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

import static org.tonguetied.usermanagement.User.FIELD_EMAIL;
import static org.tonguetied.usermanagement.User.FIELD_FIRSTNAME;
import static org.tonguetied.usermanagement.User.FIELD_LASTNAME;
import static org.tonguetied.usermanagement.User.FIELD_PASSWORD;
import static org.tonguetied.usermanagement.User.FIELD_REPEATED_PASSWORD;
import static org.tonguetied.usermanagement.User.FIELD_USERNAME;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.tonguetied.usermanagement.User;
import org.tonguetied.usermanagement.UserRight;
import org.tonguetied.usermanagement.UserRight.Permission;

/**
 * Value object used to transfer the user form submission details.
 * 
 * @author bsion
 *
 */
public class UserForm
{
    public static final String FIELD_USER_EMAIL = "user." + FIELD_EMAIL;
    public static final String FIELD_USER_FIRSTNAME = "user." + FIELD_FIRSTNAME;
    public static final String FIELD_USER_LASTNAME = "user." + FIELD_LASTNAME;
    public static final String FIELD_USER_PASSWORD = "user." + FIELD_PASSWORD;
    public static final String FIELD_USER_REPEATED_PASSWORD = "user." + FIELD_REPEATED_PASSWORD;
    public static final String FIELD_USER_USERNAME = "user." + FIELD_USERNAME;

    private User user;
    private Map<Permission, Boolean> permissions;
    
    /**
     * Create a new instance of UserForm.
     *
     */
    public UserForm()
    {
        super();
    }
    
    /**
     * Create a new instance of UserForm.
     *
     * @param user
     */
    public UserForm(User user)
    {
        this.user = user;
        this.permissions = new HashMap<Permission, Boolean>();
        for (Permission permission: Permission.values())
        {
            permissions.put(permission, hasPermission(permission));
        }
    }
    
    private boolean hasPermission(final Permission permission)
    {
        return this.user.getUserRights().contains(
                new UserRight(permission, null, null, null));
    }

    /**
     * @return the user
     */
    public User getUser()
    {
        return user;
    }

    /**
     * Assign the user.
     *
     * @param user the user to set
     */
    public void setUser(User user)
    {
        this.user = user;
    }
    /**
     * @return the permissions
     */
    public Map<Permission, Boolean> getPermissions()
    {
        return permissions;
    }
    /**
     * Assign the permissions.
     *
     * @param permissions the permissions to set
     */
    public void setPermissions(Map<Permission, Boolean> permissions)
    {
        this.permissions = permissions;
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this, 
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
