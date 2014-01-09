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
import static org.tonguetied.web.Constants.COUNTRY;
import static org.tonguetied.web.Constants.KEYWORD_ID;
import static org.tonguetied.web.Constants.LANGUAGE;
import static org.tonguetied.web.Constants.LANGUAGES;
import static org.tonguetied.web.Constants.STATES;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.KeywordFactory;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.TranslationIdPredicate;
import org.tonguetied.keywordmanagement.TranslationPredicate;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;


/**
 * Manage the submission of {@link Keyword}s. This controller is responsible 
 * for either creating a new {@link Keyword} or saving an existing one. 
 * {@link Keyword}s can be created either by {@link Language} or by 
 * {@link Country}
 *  
 * @author bsion
 * @see KeywordFactory#createKeyword(java.util.List, Country, Bundle)
 * @see KeywordFactory#createKeyword(java.util.List, Language, Bundle)
 */
public class KeywordController extends CancellableFormController
{
    
private KeywordService keywordService;
    
    private static final Logger logger = 
        Logger.getLogger(KeywordController.class);
    private final String KEYWORD_VIEW_NAME = 
        "redirect:/keyword.htm?"+KEYWORD_ID+"=";

    private PreferenceForm viewPreferences;
    
    /**
     * Create new instance of KeywordController 
     */
    public KeywordController()
    {
        setCommandClass(Keyword.class);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) 
            throws Exception
    {
        final Long id = RequestUtils.getLongParameter(request, KEYWORD_ID);
        Keyword keyword;
        if (id == null)
        {
            final String creationType = request.getParameter("creationType");
            final Bundle bundle = keywordService.getDefaultBundle();
            if (LANGUAGE.equals(creationType))
            {
                keyword = 
                    KeywordFactory.createKeyword(keywordService.getLanguages(), 
                        keywordService.getCountry(CountryCode.DEFAULT), bundle);
            }
            else if (COUNTRY.equals(creationType))
            {
                keyword = 
                    KeywordFactory.createKeyword(keywordService.getCountries(), 
                        keywordService.getLanguage(LanguageCode.DEFAULT), bundle);
            } 
            else
            {
                // catch all if neither is specified. This is a rare occurence
                // and generally will not occur. This case is only to prevent
                // NPE when rendering the form
                keyword = new Keyword();
            }
        }
        else
        {
            keyword = keywordService.getKeyword(id);
            final boolean isFilterApplied = isFilterApplied(request.getSession());
            if (isFilterApplied)
            {
                keyword = keyword.deepClone();
                final PreferenceFilter filter = new PreferenceFilter(viewPreferences);
                // do a deep copy of keywords to detach from Hibernate session
                CollectionUtils.filter(keyword.getTranslations(), filter);
            }
        }
        
        return keyword;
    }

    private boolean isFilterApplied(final HttpSession session)
    {
        final Boolean isFilterApplied = 
            (Boolean) session.getAttribute(APPLY_FILTER);
        return BooleanUtils.toBoolean(isFilterApplied);
    }

    @Override
    protected boolean isFormSubmission(HttpServletRequest request)
    {
        return super.isFormSubmission(request) || 
            isDeleteTranslationRequest(request);
    }

    /**
     * Determine if this request was a delete translation request.
     * 
     * @param request the current request to process
     * @return <code>true</code> if the request was to delete translations,
     * <code>false</code> otherwise
     */
    private boolean isDeleteTranslationRequest(HttpServletRequest request)
    {
        return request.getParameter("deleteTranslation") != null;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    Object command,
                                    BindException errors) throws Exception
    {
        Keyword keyword = (Keyword) command;
        
        final boolean isFilterApplied = isFilterApplied(request.getSession());
        ModelAndView modelAndView;
        if (request.getParameter("add") != null)
        {
            if (logger.isDebugEnabled()) logger.debug("adding translation");
            modelAndView = 
                addTranslation(request, response, keyword, errors, isFilterApplied);
        }
        else if (request.getParameter("delete") != null)
        {
            if (logger.isDebugEnabled()) logger.debug("deleting keyword");
            modelAndView = deleteKeyword(keyword);
        }
        else if (request.getParameter("deleteTranslation") != null)
        {
            if (logger.isDebugEnabled()) logger.debug("deleting translation");
            final Long translationId = 
                RequestUtils.getLongParameter(request, "deleteTranslation");
            modelAndView = 
                deleteTranslation(keyword, translationId, isFilterApplied);
        }
        else
        {
            if (logger.isDebugEnabled()) logger.debug("saving keyword");
            
            modelAndView = 
                saveKeyword(request, response, keyword, errors, isFilterApplied);
        }
        
        return modelAndView;
    }

    @Override
    protected ModelAndView onCancel(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    Object command) throws Exception
    {
        return new ModelAndView(getCancelView());
    }
    
    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) 
            throws Exception 
    {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(LANGUAGES, keywordService.getLanguages());
        model.put(COUNTRIES, keywordService.getCountries());
        model.put(BUNDLES, keywordService.getBundles());
        model.put(STATES, TranslationState.values());
        
        return model;
    }

    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) 
            throws Exception
    {
        binder.registerCustomEditor(Language.class,  
                new LanguageSupport(keywordService.getLanguages()));
        binder.registerCustomEditor(Country.class, 
                new CountrySupport(keywordService.getCountries()));
        binder.registerCustomEditor(Bundle.class, 
                new BundleSupport(keywordService.getBundles()));
        binder.registerCustomEditor(TranslationState.class, 
                new TranslationStateSupport());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    /**
     * Save changes to the current {@link Keyword}.
     * 
     * @param keyword the current keyword
     * @param isFilterApplied flag indicating if the view filter should be 
     * applied to the translations
     * @return the ModelAndView to render
     */
    private ModelAndView saveKeyword(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    Keyword keyword, 
                                    BindException errors, 
                                    final boolean isFilterApplied)
            throws Exception
    {
        Set<Long> origIds = new HashSet<Long>();
        if (isFilterApplied)
        {
            for (Translation translation : keyword.getTranslations())
            {
                origIds.add(translation.getId());
            }
            mergeFilteredTranslations(keyword, errors);
        }

        ModelAndView mav;
        if (!errors.hasErrors())
        {
            keywordService.saveOrUpdate(keyword);
            mav = new ModelAndView(getSuccessView());
        }
        else 
        {
            if (isFilterApplied)
            {
                final TranslationIdPredicate predicate = 
                    new TranslationIdPredicate(origIds);
                CollectionUtils.filter(keyword.getTranslations(), predicate);
            }
            mav = showForm(request, response, errors);
        }
        
        return mav;
    }

    /**
     * Merge the filtered out {@link Translation}'s back into the filtered 
     * translation set of the keyword.
     * 
     * @param keyword the current keyword
     * @param errors
     */
    private void mergeFilteredTranslations(final Keyword keyword, 
            BindException errors)
    {
        final Keyword existingKeyword =
            keywordService.getKeyword(keyword.getId());
        TranslationIdPredicate predicate;
        Translation existingTranslation;
        for (Translation translation : existingKeyword.getTranslations())
        {
            predicate = new TranslationIdPredicate(translation.getId());
            existingTranslation = (Translation) CollectionUtils.find(
                    keyword.getTranslations(), predicate);
            if (existingTranslation == null)
            {
                keyword.addTranslation(translation);
            }
        }
        KeywordValidator validator = (KeywordValidator) getValidator();
        validator.validateDuplicates(keyword.getTranslations(), errors);
    }
    
    /**
     * Remove the current {@link Keyword}.
     * 
     * @param keyword the keyword to remove
     * @return the ModelAndView to render
     */
    private ModelAndView deleteKeyword(Keyword keyword)
    {
        keywordService.delete(keyword);
        
        return new ModelAndView(getSuccessView());
    }

    /**
     * Add a {@link Translation} to the current {@link Keyword}. Before the 
     * {@linkplain Translation} is added, check the {@linkplain Keyword} does
     * not already to contain a duplicate entry.
     * 
     * @param keyword the current keyword
     * @param errors the validation errors object
     * @param isFilterApplied flag indicating if the view filter should be 
     * applied to the translations
     * @return the ModelAndView to render
     */
    private ModelAndView addTranslation(HttpServletRequest request, 
                                        HttpServletResponse response, 
                                        Keyword keyword,
                                        BindException errors,
                                        final boolean isFilterApplied)
            throws Exception
    {
        if (logger.isDebugEnabled())
            logger.debug("adding new translation to keyword");
        
        if (isFilterApplied)
        {
            keyword = keywordService.getKeyword(keyword.getId());
        }
        KeywordValidator validator = (KeywordValidator) getValidator();
        validator.validateDuplicates(keyword.getTranslations(), 
                new TranslationPredicate(null, null, null), errors);

        ModelAndView mav;
        if (!errors.hasErrors())
        {
            keyword.addTranslation(new Translation());
            keywordService.saveOrUpdate(keyword);
            mav = new ModelAndView(KEYWORD_VIEW_NAME + keyword.getId());
        }
        else 
        {
            mav = showForm(request, response, errors);
        }
        
        return mav;
    }
    
    /**
     * Perform steps to remove a translation from the current keyword.
     */
    private ModelAndView deleteTranslation(Keyword keyword, 
            final Long translationId, final boolean isFilterApplied)
            throws Exception
    {
        if (logger.isDebugEnabled())
            logger.debug("removing translation from keyword");

        if (isFilterApplied)
        {
            keyword = keywordService.getKeyword(keyword.getId());
        }
        keyword.removeTranslation(translationId);
        keywordService.saveOrUpdate(keyword);

        return new ModelAndView(KEYWORD_VIEW_NAME + keyword.getId());
    }

    /**
     * Assign the {@link KeywordService}.
     * 
     * @param keywordService the {@link KeywordService} to set.
     */
    public void setKeywordService(final KeywordService keywordService)
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
