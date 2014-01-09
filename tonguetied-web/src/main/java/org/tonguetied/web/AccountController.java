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
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.tonguetied.usermanagement.User;
import org.tonguetied.usermanagement.UserService;


/**
 * Controller responsible for the action logic of a User updating their account
 * details.
 * 
 * @author bsion
 *
 */
public class AccountController extends SimpleFormController
{
    private static final String ACTION_MESSAGE = "actionMessage";

    private static final Logger logger = 
        Logger.getLogger(AccountController.class);

    private UserService userService;
    
    /**
     * Create new instance of AccountController.
     */
    public AccountController() 
    {
        setCommandClass(AccountForm.class);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) 
            throws Exception 
    {
        User user = getCurrentUser();
        
        return new AccountForm(
                user.getFirstName(), user.getLastName(), user.getEmail());
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    Object command,
                                    BindException errors) throws Exception 
    {
        if (logger.isDebugEnabled()) 
            logger.debug("saving account details for user");
        AccountForm form = (AccountForm) command;
        
        User user = getCurrentUser();
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        
        userService.saveOrUpdate(user);
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(getCommandName(), command);
        model.put(ACTION_MESSAGE, "account.action.submitted");
        return new ModelAndView(getSuccessView(), model);
    }

    /**
     * Find the currently logged in {@link User}.
     * 
     * @return the currently logged in user.
     */
    private User getCurrentUser()
    {
        UserDetails userDetails = 
            (UserDetails) SecurityContextHolder.getContext().
            getAuthentication().getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        return user;
    }

    /**
     * Assign the {@link UserService}.
     * 
     * @param userService the {@link UserService} to set.
     */
    public void setUserService(UserService userService) 
    {
        this.userService = userService;
    }
}
