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

import static org.tonguetied.web.Constants.AUDIT_LOG;
import static org.tonguetied.web.Constants.BUNDLES;
import static org.tonguetied.web.Constants.BUNDLE_ID;
import static org.tonguetied.web.Constants.COUNTRIES;
import static org.tonguetied.web.Constants.COUNTRY_ID;
import static org.tonguetied.web.Constants.DEFAULT_AUDIT_LOG_PAGE_SIZE;
import static org.tonguetied.web.Constants.DEFAULT_USER_PAGE_SIZE;
import static org.tonguetied.web.Constants.KEYWORDS;
import static org.tonguetied.web.Constants.KEYWORD_ID;
import static org.tonguetied.web.Constants.LANGUAGES;
import static org.tonguetied.web.Constants.LANGUAGE_ID;
import static org.tonguetied.web.Constants.MAX_LIST_SIZE;
import static org.tonguetied.web.Constants.PAGE_SIZES;
import static org.tonguetied.web.Constants.SEARCH_PARAMETERS;
import static org.tonguetied.web.Constants.SHOW_ALL_KEYWORDS;
import static org.tonguetied.web.Constants.SHOW_ALL_USERS;
import static org.tonguetied.web.Constants.STATES;
import static org.tonguetied.web.Constants.TABLE_ID_KEYWORD;
import static org.tonguetied.web.Constants.TABLE_ID_USER;
import static org.tonguetied.web.Constants.USERS;
import static org.tonguetied.web.Constants.USER_SIZE;
import static org.tonguetied.web.Constants.VIEW_PREFERENCES;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.SetUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.tonguetied.audit.AuditLogRecord;
import org.tonguetied.audit.AuditService;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.ReferenceException;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Translation.TranslationState;
import org.tonguetied.usermanagement.User;
import org.tonguetied.usermanagement.UserService;
import org.tonguetied.utils.pagination.Order;
import org.tonguetied.utils.pagination.PaginatedList;
import org.tonguetied.web.PaginationUtils.KeyValue;


/**
 * Controller implementation that processes various HTTP requests for the main
 * page.
 * 
 * @author bsion
 *
 */
public class MainController extends MultiActionController
{
    private KeywordService keywordService;
    private UserService userService;
    private AuditService auditService;
    private PreferenceForm viewPreferences;
    private SearchForm searchParameters;
    private User userSearch;

    private static final int[] KEYWORD_PAGE_SIZE_OPTIONS = {10, 25, 50};

    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain KeywordService#getKeywords()} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView keywords(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        Cookie cookie = CookieUtils.getCookie(request, "menuSelected");
        if (cookie == null)
        {
            cookie = CookieUtils.createCookie(request, "menuSelected", "1");
            response.addCookie(cookie);
        }
        
        Boolean showAll = RequestUtils.getBooleanParameter(request, SHOW_ALL_KEYWORDS);
        if (showAll == null)
        {
            showAll = (Boolean) request.getSession().getAttribute(SHOW_ALL_KEYWORDS);
        }
        
        final int firstResult = PaginationUtils.calculateFirstResult(
           TABLE_ID_KEYWORD, viewPreferences.getMaxResults(), request);
        final KeyValue<String, Order> keyValue = 
            PaginationUtils.getOrder(TABLE_ID_KEYWORD, request);
        Order order = null;
        if (keyValue != null)
            order = keyValue.getValue();
        
        PaginatedList<Keyword> keywords;
        if (showAll)
        {
            keywords = keywordService.getKeywords(firstResult,
                    viewPreferences.getMaxResults(), order);
            searchParameters.initialize();
        }
        else
        {
            Keyword keyword = searchParameters.getKeyword();
            if (new Translation().equals(keyword.getTranslations().first()))
            {
                keyword.setTranslations(SetUtils.EMPTY_SORTED_SET);
            }
            keywords = 
                keywordService.findKeywords(keyword,
                        searchParameters.getIgnoreCase(),
                        order,
                        firstResult,
                        viewPreferences.getMaxResults());
        }
        
        keywords = applyViewPreferences(keywords);
        searchParameters.getKeyword();

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(KEYWORDS, keywords);
        model.put(LANGUAGES, keywordService.getLanguages());
        model.put(BUNDLES, keywordService.getBundles());
        model.put(COUNTRIES, keywordService.getCountries());
        model.put(STATES, TranslationState.values());
        model.put(SEARCH_PARAMETERS, searchParameters);
        model.put(VIEW_PREFERENCES, viewPreferences);
        model.put(MAX_LIST_SIZE, keywords.getMaxListSize());
        model.put(PAGE_SIZES, KEYWORD_PAGE_SIZE_OPTIONS);
        return new ModelAndView("keyword/keywords", model);
    }

    /**
     * Filters out the translations that the user selected in the 
     * {@link PreferenceForm}.
     * 
     * @param keywords the list of {@link Keyword}s to apply the filter to
     * @throws CloneNotSupportedException if one of the objects in the list is
     * not cloneable
     */
    private PaginatedList<Keyword> applyViewPreferences(
            final PaginatedList<Keyword> keywords)
            throws CloneNotSupportedException
    {
        final PreferenceFilter filter = new PreferenceFilter(viewPreferences);
        // do a deep copy of keywords to detach from hibernate session
        PaginatedList<Keyword> clone = keywords.deepClone();
        for (final Keyword keyword : clone)
        {
            CollectionUtils.filter(keyword.getTranslations(), filter);
        }
        
        return clone;
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain KeywordService#getBundles()} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView bundles(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        List<Bundle> bundles = keywordService.getBundles();
        
        return new ModelAndView("bundle/bundles", BUNDLES, bundles);
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain KeywordService#getCountries()} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView countries(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        List<Country> countries = keywordService.getCountries();
        
        return new ModelAndView("country/countries", COUNTRIES, countries);
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain KeywordService#getLanguages()} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView languages(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        List<Language> languages = keywordService.getLanguages();
        
        return new ModelAndView("language/languages", LANGUAGES, languages);
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain UserService#getUsers()} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView users(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        Boolean showAll = RequestUtils.getBooleanParameter(request, SHOW_ALL_USERS);
        if (showAll == null)
        {
            showAll = (Boolean) request.getSession().getAttribute(SHOW_ALL_USERS);
        }
        
        final int firstResult = PaginationUtils.calculateFirstResult(
                TABLE_ID_USER, DEFAULT_USER_PAGE_SIZE, request);

        PaginatedList<User> users;
        if (showAll)
        {
            users = userService.getUsers(firstResult, DEFAULT_USER_PAGE_SIZE);
            userSearch.initialize();
        }
        else
        {
            users = userService.findUsers(userSearch, firstResult, DEFAULT_USER_PAGE_SIZE);
        }
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(USERS, users);
        model.put(USER_SIZE, DEFAULT_USER_PAGE_SIZE);
        model.put(MAX_LIST_SIZE, users.getMaxListSize());
        model.put("userSearch", userSearch);
        
        return new ModelAndView("user/users", model);
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain AuditService#getAuditLog()} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView auditLog(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        final int firstResult = PaginationUtils.calculateFirstResult(
                "record", DEFAULT_AUDIT_LOG_PAGE_SIZE, request);

        final PaginatedList<AuditLogRecord> auditLog = 
            auditService.getAuditLog(firstResult, DEFAULT_AUDIT_LOG_PAGE_SIZE);
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(AUDIT_LOG, auditLog);
        model.put("auditLogSize", DEFAULT_AUDIT_LOG_PAGE_SIZE);
        model.put(MAX_LIST_SIZE, auditLog.getMaxListSize());

        return new ModelAndView("audit/auditLog", model);
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain KeywordService#deleteKeyword(Long)} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView deleteKeyword(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        final Long keywordId = 
            RequestUtils.getLongParameter(request, KEYWORD_ID);
        keywordService.deleteKeyword(keywordId);
        
        return new ModelAndView("redirect:/keywords.htm");
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain KeywordService#deleteBundle(Long)} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView deleteBundle(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        final Long bundleId = 
            RequestUtils.getLongParameter(request, BUNDLE_ID);
        Map<String, Object> model = new HashMap<String, Object>();
        try
        {
            keywordService.deleteBundle(bundleId);
        }
        catch (ReferenceException re)
        {
            model.put("error", re);
        }
        
        return new ModelAndView("forward:/bundles.htm", model);
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain KeywordService#deleteCountry(Long)} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView deleteCountry(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        final Long countryId = 
            RequestUtils.getLongParameter(request, COUNTRY_ID);
        Map<String, Object> model = new HashMap<String, Object>();
        try
        {
            keywordService.deleteCountry(countryId);
        }
        catch (ReferenceException re)
        {
            model.put("error", re);
        }
        
        return new ModelAndView("forward:/countries.htm", model);
    }
    
    /**
     * Handler method that acts as an HTTP interface to the 
     * {@linkplain KeywordService#deleteCountry(Long)} method.
     * 
     * @param request the current HTTP request.
     * @param response the current HTTP response.
     * @return a ModelAndView to render.
     * @throws Exception in case of errors.
     */
    public ModelAndView deleteLanguage(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        final Long languageId = 
            RequestUtils.getLongParameter(request, LANGUAGE_ID);
        Map<String, Object> model = new HashMap<String, Object>();
        try
        {
            keywordService.deleteLanguage(languageId);
        }
        catch (ReferenceException re)
        {
            model.put("error", re);
        }
        
        return new ModelAndView("forward:/languages.htm", model);
    }
    
    /**
     * Assign the {@link KeywordService}.
     * 
     * @param keywordService the {@link KeywordService} to set
     */
    public void setKeywordService(KeywordService keywordService)
    {
        this.keywordService = keywordService;
    }
    
    /**
     * Assign the {@link UserService}.
     * 
     * @param userService the {@link UserService} to set
     */
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }

    /**
     * Assign the {@link AuditService}.
     * 
     * @param auditService the {@link AuditService} to set
     */
    public void setAuditService(AuditService auditService)
    {
        this.auditService = auditService;
    }
    
    /**
     * Assign the {@link PreferenceForm}.
     * 
     * @param viewPreferences the {@link PreferenceForm} to set
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

    /**
     * Assign the {@link User} object used to as the search parameters.
     * 
     * @param userSearch the {@link User} to set
     */
    public void setUserSearch(User userSearch)
    {
        this.userSearch = userSearch;
    }
}
