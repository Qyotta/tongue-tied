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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.tonguetied.usermanagement.AuthenticationException;
import org.tonguetied.usermanagement.User;
import org.tonguetied.usermanagement.UserService;

/**
 * Controller object responsible for the action logic of a {@link User} 
 * changing their password.
 * 
 * @author bsion
 *
 */
public class ChangePasswordController extends CancellableFormController
{
    private UserService userService;

    /**
     * Create a new instance of ChangePasswordController.
     */
    public ChangePasswordController()
    {
        this.setCommandClass(ChangePasswordForm.class);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {
        ModelAndView mav;
        ChangePasswordForm form = (ChangePasswordForm) command;
        try 
        {
            this.userService.changePassword(
                getCurrentUser(), form.getOldPassword(), form.getNewPassword());
            mav = new ModelAndView(getSuccessView());
        }
        catch (AuthenticationException ae)
        {
            errors.rejectValue("oldPassword", "error.invalid.password");
            mav = showForm(request, response, errors);
        }
        
        return mav;
    }

    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object command) throws Exception
    {
        return new ModelAndView(getCancelView());
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

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        ChangePasswordForm form = new ChangePasswordForm();
        return form;
    }

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
