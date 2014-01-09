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

import static org.tonguetied.web.Constants.BTN_SEARCH;
import static org.tonguetied.web.Constants.SHOW_ALL_USERS;
import static org.tonguetied.web.Constants.TABLE_ID_USER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tonguetied.usermanagement.User;

/**
 * Controller for processing user search requests.
 * 
 * @author bsion
 * 
 */
public class UserSearchController extends SimpleFormController
{
    private User userSearch;

    private static final Logger logger = 
        Logger.getLogger(UserSearchController.class);

    /**
     * Create new instance of KeywordSearchController
     */
    public UserSearchController()
    {
        setCommandClass(User.class);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        return userSearch;
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
        if (logger.isDebugEnabled()) logger.debug("searching for users");
        PaginationUtils.remove(TABLE_ID_USER, request);
        request.getSession().setAttribute(SHOW_ALL_USERS, false);

        return new ModelAndView(getSuccessView());
    }

    @Override
    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) throws Exception
    {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
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
