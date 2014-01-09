/*
 * Copyright 2008 The Tongue-Tied Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License. 
 */
package org.tonguetied.utils.database;

import static org.tonguetied.utils.database.Constants.DB_NAME;
import static org.tonguetied.utils.database.Constants.KEY_JDBC_DRIVER;
import static org.tonguetied.utils.database.Constants.KEY_JDBC_NAME;
import static org.tonguetied.utils.database.Constants.KEY_JDBC_PORT;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.DialectFactory;
import org.hibernate.dialect.HSQLDialect;
import org.hsqldb.Server;

/**
 * Wrapper around the HSQLDB embedded database server. This wrapper exposes
 * methods needed by clients to use the embedded database and centralises those
 * calls in one place.
 * 
 * @author bsion
 *
 */
public final class EmbeddedDatabaseServer
{
    private static Server dbServer = new Server();
    private static final Logger logger = 
        Logger.getLogger(EmbeddedDatabaseServer.class);

    static Server getServer()
    {
        return dbServer;
    }
    
    /**
     * Start the embedded database if it is not already running. If it is 
     * already running then do nothing.
     *
     * @param props the Properties containing server configuration information
     */
    public static synchronized void startDatabase(Properties props)
    {
        if (props == null)
            throw new IllegalArgumentException("must supply valid properties");
        if (!isRunning())
        {
            if (dbServer == null)
                dbServer = new Server();
            // If the server is not running then start the server
            if (logger.isInfoEnabled())
                logger.info("Db server has not been started. Attempting to start");

            // initialize DB...
            final String dbName = props.getProperty(KEY_JDBC_NAME, DB_NAME);
            final String serverPath = "data" +File.separator+ dbName;
            dbServer.setDatabasePath(0, serverPath);// +";shutdown=true");
            dbServer.setDatabaseName(0, dbName);
            dbServer.setPort(Integer.parseInt(props.getProperty(KEY_JDBC_PORT)));
            dbServer.start();

            try
            {
                Class.forName(props.getProperty(KEY_JDBC_DRIVER));
            }
            catch (ClassNotFoundException cnfe)
            {
                logger.error("ERROR: failed to load HSQLDB JDBC driver.");
                cnfe.printStackTrace();
                return;
            }
        }
    }
    
    /**
     * Stop the embedded database if it is running.
     *
     */
    public static synchronized void shutdownDatabase()
    {
        if (logger.isInfoEnabled()) 
            logger.info("Attempting to shutdown database server");
        if (dbServer != null)
            dbServer.shutdown();
        dbServer = null;
    }

    /**
     * Stop the embedded database if it is running.
     *
     */
    public static synchronized void stopDatabase()
    {
        if (logger.isInfoEnabled()) 
            logger.info("Attempting to stop database server");
        if (dbServer != null)
            dbServer.stop();
        dbServer = null;
    }

    /**
     * Determine if an embedded database server is running.
     * 
     * @return <code>true</code> if the server is running, <code>false</code>
     * otherwise
     */
    public static boolean isRunning()
    {
        boolean isRunning = true;
        try
        {
            // ensure the db server is running
            if (logger.isInfoEnabled())
                logger.info("checking if DB is already started");
            dbServer.checkRunning(true);
        }
        catch (RuntimeException re)
        {
            isRunning = false;
        }
        return isRunning;
    }

    /**
     * Evaluate the <code>dialect</code> to determine if this database type can
     * be run as an embedded database.
     * 
     * @param dialectStr the database dialect to evaluate
     * @return <code>true</code> if the database type allows embedding, 
     * <code>false</code> otherwise
     */
    public static boolean isEmbeddable(final String dialectStr)
    {
        boolean result = false;
        if (dialectStr != null)
        {
            try
            {
                final Dialect dialect = DialectFactory.buildDialect(dialectStr);
                result = dialect instanceof HSQLDialect;
            }
            catch (HibernateException he)
            {
                logger.warn("failed to evaluate dialect: " + dialectStr);
            }
        }
        return result;
    }
}
