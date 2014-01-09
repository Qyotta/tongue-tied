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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Language.LanguageCode;


/**
 * Manage the submission of {@link Language}s. This controller is responsible 
 * for either creating a new {@link Language} or saving an existing one. 
 * 
 * @author bsion
 *
 */
public class LanguageController extends CancellableFormController
{
    private KeywordService keywordService;
    
    private static final Logger logger = 
        Logger.getLogger(LanguageController.class);

    /**
     * Create new instance of LanguageController 
     */
    public LanguageController()
    {
        setCommandClass(Language.class);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) 
            throws Exception
    {
        final Long id = RequestUtils.getLongParameter(request, "languageId");
        Language language = keywordService.getLanguage(id);
        if (language == null)
            language = new Language();
        
        return language;
    }

    /**
     * 
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    Object command,
                                    BindException errors) throws Exception
    {
        if (logger.isDebugEnabled()) logger.debug("saving language");
        Language language = (Language) command;
        
        keywordService.saveOrUpdate(language);
        
        return new ModelAndView(getSuccessView());
    }

    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object command) throws Exception
    {
        return new ModelAndView(getCancelView());
    }

    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) 
            throws Exception
    {
        binder.registerCustomEditor(LanguageCode.class, 
                new LanguageCodeSupport());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) 
            throws Exception 
    {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("languageCodes", LanguageCode.values());
        
        return model;
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
}
