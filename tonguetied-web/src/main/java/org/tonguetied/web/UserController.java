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
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.tonguetied.usermanagement.User;
import org.tonguetied.usermanagement.UserRight;
import org.tonguetied.usermanagement.UserService;
import org.tonguetied.usermanagement.UserRight.Permission;

/**
 * Controller responsible for action logic around the creating or updating of a
 * {@link User}.
 * 
 * @author bsion
 * 
 */
public class UserController extends CancellableFormController
{

    private UserService userService;

    private static final Logger logger = Logger.getLogger(UserController.class);

    /**
     * Create new instance of UserController
     */
    public UserController()
    {
        setCommandClass(UserForm.class);
    }

    @Override
    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) throws Exception
    {
        binder.registerCustomEditor(Permission.class, new PermissionSupport());
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request)
            throws Exception
    {
        Map<String, Object> model = new HashMap<String, Object>();
//        model.put("permissions", Permission.values());
        Map<Permission, Boolean> permissions = new HashMap<Permission, Boolean>();
        for (Permission permission: Permission.values())
        {
            permissions.put(permission, false);
        }
        model.put("permissionsMap", permissions);

        return model;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        final Long id = RequestUtils.getLongParameter(request, "id");
        User user = null;
        if (id != null)
        {
            if (logger.isDebugEnabled()) 
                logger.debug("looking for user with id: " + id);
            user = userService.getUser(id);
        }

        if (user == null)
        {
            if (logger.isDebugEnabled()) 
                logger.debug("creating new user");
            user = new User();
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            UserRight userRight = new UserRight(Permission.ROLE_USER, null,
                    null, null);
            userRight.setUser(user);
            user.addUserRight(userRight);
        }

        return new UserForm(user);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {
        if (logger.isDebugEnabled()) logger.debug("saving user");
        final UserForm userForm = (UserForm) command;
        User user = userForm.getUser();
        
        for (final Entry<Permission, Boolean> entry: userForm.getPermissions().entrySet())
        {
            final UserRight userRight = 
                new UserRight(entry.getKey(), null, null, null);
            if (entry.getValue())
                user.addUserRight(userRight);
            else
                user.removeUserRight(userRight);
        }

        userService.saveOrUpdate(user);

        return new ModelAndView(getSuccessView());
    }

    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
            HttpServletResponse response, Object command) throws Exception
    {
        return new ModelAndView(getCancelView());
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
