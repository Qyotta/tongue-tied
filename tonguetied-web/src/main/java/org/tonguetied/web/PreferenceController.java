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

import static org.tonguetied.web.Constants.APPLY_FILTER;
import static org.tonguetied.web.Constants.BUNDLES;
import static org.tonguetied.web.Constants.COUNTRIES;
import static org.tonguetied.web.Constants.LANGUAGES;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;


/**
 * Controller for processing preference setting requests.
 * 
 * @author bsion
 *
 */
public class PreferenceController extends SimpleFormController
{
    private KeywordService keywordService;
    private PreferenceForm viewPreferences;
    
    private static final Logger logger = 
        Logger.getLogger(PreferenceController.class);

    public PreferenceController()
    {
        setCommandClass(PreferenceForm.class);
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) 
            throws Exception
    {
        return viewPreferences;
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
            
            final String submitBtn = request.getParameter("submitBtn");
            if (submitBtn != null)
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
                                    HttpServletResponse response,
                                    Object command,
                                    BindException errors) throws Exception
    {
        if (logger.isDebugEnabled()) 
            logger.debug("setting view preferences");
        
        request.getSession().setAttribute(APPLY_FILTER, 
                viewPreferences.isTranslationFilterApplied());

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
        
        return model;
    }

    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) 
            throws Exception
    {
        binder.registerCustomEditor(List.class, "selectedLanguages", new CustomCollectionEditor(List.class){
            @Override
            protected Object convertElement(Object element) {
                Language language = null;
                if (element != null) {
                    Long id = new Long((String)element);
                    language = keywordService.getLanguage(id);
                }
                return language;
            }
        });
        binder.registerCustomEditor(List.class, "selectedCountries", new CustomCollectionEditor(List.class){
            @Override
            protected Object convertElement(Object element) {
                Country country = null;
                if (element != null) {
                    Long id = new Long((String)element);
                    country = keywordService.getCountry(id);
                }
                return country;
            }
        });
        binder.registerCustomEditor(List.class, "selectedBundles", new CustomCollectionEditor(List.class){
            @Override
            protected Object convertElement(Object element) {
                Bundle bundle = null;
                if (element != null) {
                    Long id = new Long((String)element);
                    bundle = keywordService.getBundle(id);
                }
                return bundle;
            }
        });
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
}
