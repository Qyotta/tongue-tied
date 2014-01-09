/*
 * Copyright 2008 The Tongue-Tied Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.tonguetied.web.servlet;

import static org.tonguetied.utils.database.Constants.KEY_HIBERNATE_DIALECT;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.tonguetied.administration.AdministrationService;
import org.tonguetied.administration.ServerData;
import org.tonguetied.utils.database.EmbeddedDatabaseServer;

/**
 * Listener that initializes application wide settings when the HTTP server is
 * first started and tidies up any resources when the servletContext is
 * destroyed.
 * 
 * @author bsion
 * 
 */
public class ServletContextInitializer implements ServletContextListener {
    private static final String DIR_WEB_INF = "/WEB-INF";
    private static final String DIR_SQL = DIR_WEB_INF + "/sql";
    // private static final String DIR_SQL_UPDATE = DIR_SQL + "/update";
    private static final String BUILD_DATE_FORMAT = "yyyyMMdd-hhmm";
    private static final String KEY_SUPPORTED_LANGUAGES = "supportedLanguages";
    private static final String LANGUAGE_PROPERTIES = "language";
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(BUILD_DATE_FORMAT);
    private static final Logger logger = Logger.getLogger(ServletContextInitializer.class);

    /**
     * Perform a graceful shutdown of the server servlets. If the server is
     * running an embedded database, then this is shut down here.
     * 
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
        Properties props = System.getProperties();
        final String dialect = props.getProperty(KEY_HIBERNATE_DIALECT);
        // shutdown the embedded database if it is running
        if (EmbeddedDatabaseServer.isEmbeddable(dialect))
            EmbeddedDatabaseServer.stopDatabase();
    }

    /**
     * Perform server servlet initialization. When the servlet is first
     * initialized, the server system properties are set. These server
     * properties are used in the domain layer, but are not known until the web
     * application is deployed and started. Also loads application wide
     * variables including:
     * <ul>
     * <li>the list of supported languages</li>
     * </ul>
     * 
     * This method also performs database management. It determines if this
     * server should run an embedded database, ie it is in demo mode, and starts
     * the database. It also creates the database, the first time it connects to
     * the server.
     * 
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        loadSupportedLanguages(event.getServletContext());

        WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        AdministrationService administrationService = (AdministrationService) applicationContext.getBean("administrationService");
        Properties props = System.getProperties();
        final String dialect = props.getProperty(KEY_HIBERNATE_DIALECT);
        if (EmbeddedDatabaseServer.isEmbeddable(dialect))
            EmbeddedDatabaseServer.startDatabase(props);
        ServerData serverData = administrationService.getLatestData();
        // If server data does not exist then assume the database has not been
        // created
        if (serverData == null) {
            if (logger.isDebugEnabled())
                logger.debug("attempting to create database");

            final String[] schemas = loadSchemas(event.getServletContext(), dialect, administrationService);
            administrationService.createDatabase(schemas);
            serverData = createServerData(event.getServletContext());
            if (serverData != null)
                administrationService.saveOrUpdate(serverData);
        }
        // check if we need to update
        else {
            if (logger.isDebugEnabled())
                logger.debug("attempting to update database");

            ServerData curServerData = createServerData(event.getServletContext());
            if (curServerData.compareTo(serverData) > 0) {
                // final String[] schemas =
                // loadSchemas(event.getServletContext(), dialect,
                // curServerData.getVersion());
                // administrationService.createDatabase(schemas);
                administrationService.saveOrUpdate(curServerData);
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("Charset for this JVM: " + Charset.defaultCharset());
        }
    }

    /**
     * Create the list of supported languages used by the web front end and add
     * as a application scope variable.
     * 
     * @param context
     *            the {@linkplain ServletContext} to add the list of languages
     */
    private void loadSupportedLanguages(ServletContext context) {
        // read language.properties
        if (logger.isInfoEnabled())
            logger.info("loading resources from file: " + LANGUAGE_PROPERTIES);
        final ResourceBundle bundle = ResourceBundle.getBundle(LANGUAGE_PROPERTIES);
        Set<String> languageKeys = new TreeSet<String>(Collections.list(bundle.getKeys()));
        if (languageKeys.isEmpty()) {
            logger.warn("Resource file " + LANGUAGE_PROPERTIES + ".properties contains no entries");
            languageKeys.add(Locale.ENGLISH.getLanguage());
        }

        context.setAttribute(KEY_SUPPORTED_LANGUAGES, languageKeys);
    }

    /**
     * Create an instance of a {@link ServerData} object instantiating it from
     * the system properties.
     * 
     * @param servletContext
     * @return an instantiated {@link ServerData} object
     */
    private ServerData createServerData(ServletContext servletContext) {
        ServerData serverData;
        try {
            final ResourceBundle bundle = ResourceBundle.getBundle("buildNumber");

            serverData = new ServerData(bundle.getString("build.version"), bundle.getString("build.number"), parseBuildDate(bundle
                    .getString("build.date")));
        } catch (ParseException pe) {
            logger.error("failed to parse date", pe);
            throw new IllegalStateException("failed to parse date", pe);
        }

        return serverData;
    }

    private Date parseBuildDate(String dateString) throws ParseException {
        if (StringUtils.isEmpty(dateString) || dateString.contains("$")) {
            return new Date();
        } else {
            return DATE_FORMAT.parse(dateString);
        }
    }

    /**
     * Load the SQL schema files used to create the database.
     * 
     * @param servletContext
     * @param dialect
     *            the SQL dialect
     * @param administrationService
     *            the administration service
     * @return a string representation of each SQL file
     */
    private String[] loadSchemas(ServletContext servletContext, final String dialect, AdministrationService administrationService) {
        InputStream is = null;
        try {
            List<String> schemas = new ArrayList<String>();
            final String schemaFile = DIR_SQL + "/" + administrationService.getSchemaFileName(dialect);
            is = servletContext.getResourceAsStream(schemaFile);
            schemas.add(IOUtils.toString(is));
            is = servletContext.getResourceAsStream(DIR_SQL + "/initial-data.sql");
            schemas.add(IOUtils.toString(is));
            return schemas.toArray(new String[schemas.size()]);
        } catch (IOException ioe) {
            logger.error("failed to load file", ioe);
            throw new IllegalStateException("failed to load schemas", ioe);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
