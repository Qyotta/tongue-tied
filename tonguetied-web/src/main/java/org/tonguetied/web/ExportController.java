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
package org.tonguetied.web;

import static org.tonguetied.web.Constants.BUNDLES;
import static org.tonguetied.web.Constants.COUNTRIES;
import static org.tonguetied.web.Constants.FORMAT_TYPES;
import static org.tonguetied.web.Constants.LANGUAGES;
import static org.tonguetied.web.Constants.STATES;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.tonguetied.datatransfer.DataService;
import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.datatransfer.common.FormatType;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * Manage the submission of export requests. This controller is responsible for
 * either creating a new data export to a specified format.
 * 
 * @author bsion
 * 
 */
public class ExportController extends CancellableFormController {
    private static final String DATE_FORMAT = "yyyy-MM-dd_HH_mm_ss";

    private KeywordService keywordService;
    private DataService dataService;

    private Resource exportDir;

    private static final Logger logger = Logger.getLogger(ExportController.class);

    /**
     * Create new instance of CountryController
     */
    public ExportController() {
        setCommandClass(ExportParameters.class);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ExportParameters exportParameters = new ExportParameters();

        return exportParameters;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        if (logger.isDebugEnabled())
            logger.debug("beginning export");
        ExportParameters parameters = (ExportParameters) command;

        // Set output path.

        parameters.setOutputPath(getOutputPath());

        dataService.exportData(parameters);

        return new ModelAndView(getSuccessView());
    }

    private String getOutputPath() {
        try {
            final DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            return new File(exportDir.getFile(), formatter.format(new Date())).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create output path", e);
        }
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Language.class, new LanguageSupport(keywordService.getLanguages()));
        binder.registerCustomEditor(Country.class, new CountrySupport(keywordService.getCountries()));
        binder.registerCustomEditor(Bundle.class, new BundleSupport(keywordService.getBundles()));
        binder.registerCustomEditor(FormatType.class, new FormatTypeSupport());
        binder.registerCustomEditor(TranslationState.class, new TranslationStateSupport());
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(LANGUAGES, keywordService.getLanguages());
        model.put(COUNTRIES, keywordService.getCountries());
        model.put(BUNDLES, keywordService.getBundles());
        model.put(FORMAT_TYPES, FormatType.values());
        model.put(STATES, TranslationState.values());

        return model;
    }

    /**
     * Assign the {@link KeywordService}.
     * 
     * @param keywordService
     *            the {@link KeywordService} to set.
     */
    public void setKeywordService(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    /**
     * Assign the {@link DataService}.
     * 
     * @param dataService
     *            the {@link DataService} to set.
     */
    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setExportDir(Resource exportDir) {
        this.exportDir = exportDir;
    }
}
