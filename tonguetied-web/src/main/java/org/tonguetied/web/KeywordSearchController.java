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
package org.tonguetied.web;

import static org.tonguetied.web.Constants.BUNDLES;
import static org.tonguetied.web.Constants.COUNTRIES;
import static org.tonguetied.web.Constants.LANGUAGES;
import static org.tonguetied.web.Constants.BTN_SEARCH;
import static org.tonguetied.web.Constants.SHOW_ALL_KEYWORDS;
import static org.tonguetied.web.Constants.STATES;
import static org.tonguetied.web.Constants.VIEW_PREFERENCES;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * Controller for processing keyword / translation search requests.
 * 
 * @author bsion
 * 
 */
public class KeywordSearchController extends SimpleFormController
{

    private KeywordService keywordService;
    private PreferenceForm viewPreferences;
    private SearchForm searchParameters;

    private static final Logger logger = 
            Logger.getLogger(KeywordSearchController.class);

    /**
     * Create new instance of KeywordSearchController
     */
    public KeywordSearchController()
    {
        setCommandClass(SearchForm.class);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        return searchParameters;
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        ModelAndView mav = null;
        // Need to intercept get requests to this controller in the scenario 
        // when sent from a paginated link, so need to display the search 
        // results
        if (RequestUtils.isGetMethod(request))
        {
            if (logger.isDebugEnabled())
                logger.debug("evaluating if this a search request");
            
            final String searchBtn = request.getParameter(BTN_SEARCH);
            if (searchBtn != null)
            {
                mav = onSubmit(request, response, null, super.getErrorsForNewForm(request));
            }
        }
        
        if (mav == null)
            mav = super.handleRequest(request, response);
        
        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {
        if (logger.isDebugEnabled()) logger.debug("searching for keywords");

        request.getSession().setAttribute(SHOW_ALL_KEYWORDS, false);

        return new ModelAndView(getSuccessView());
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request)
            throws Exception
    {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(LANGUAGES, keywordService.getLanguages());
        model.put(BUNDLES, keywordService.getBundles());
        model.put(COUNTRIES, keywordService.getCountries());
        model.put(STATES, TranslationState.values());
        model.put(VIEW_PREFERENCES, viewPreferences);

        return model;
    }

    @Override
    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) throws Exception
    {
        binder.registerCustomEditor(Language.class, new LanguageSupport(
                keywordService.getLanguages()));
        binder.registerCustomEditor(Bundle.class, new BundleSupport(
                keywordService.getBundles()));
        binder.registerCustomEditor(Country.class, new CountrySupport(
                keywordService.getCountries()));
        binder.registerCustomEditor(TranslationState.class,
                new TranslationStateSupport());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(
                        true));
    }

    /**
     * Assign the {@link KeywordService}.
     * 
     * @param keywordService the {@link KeywordService} to set.
     */
    public void setKeywordService(KeywordService keywordService)
    {
        this.keywordService = keywordService;
    }

    /**
     * Assign the {@link PreferenceForm}.
     * 
     * @param viewPreferences the viewPreferences to set
     */
    public void setViewPreferences(PreferenceForm viewPreferences)
    {
        this.viewPreferences = viewPreferences;
    }

    /**
     * Assign the {@link SearchForm}.
     * 
     * @param searchParameters the searchParameters to set
     */
    public void setSearchParameters(SearchForm searchParameters)
    {
        this.searchParameters = searchParameters;
    }
}
