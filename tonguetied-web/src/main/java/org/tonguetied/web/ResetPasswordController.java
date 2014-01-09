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

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.tonguetied.usermanagement.User;
import org.tonguetied.usermanagement.UserService;

/**
 * Controller object responsible for the action logic of an super user changing
 * another {@link User}s password.
 * 
 * @author bsion
 *
 */
public class ResetPasswordController extends CancellableFormController
{
    private UserService userService;

    /**
     * Create a new instance of ChangePasswordController.
     */
    public ResetPasswordController()
    {
        this.setCommandClass(ChangePasswordForm.class);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {
        ChangePasswordForm form = (ChangePasswordForm) command;
        User user = userService.getUser(form.getUserId());
        user.setPassword(form.getNewPassword());
        userService.saveOrUpdate(user, true);
        
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
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        final Long userId = RequestUtils.getLongParameter(request, "userId");
        ChangePasswordForm form = new ChangePasswordForm();
        if (userId != null)
        {
            form.setUserId(userId);
        }
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
