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

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.tonguetied.usermanagement.User;

/**
 * Value object for the user to make changes to their account details. This
 * object will only contain the fields that a User can update in the 
 * {@link User} object. This is done for security reasons.
 * 
 * @author bsion
 *
 */
public class AccountForm
{
    private String firstName;
    private String lastName;
    private String email;
    
    /**
     * Create a new instance of AccountForm.
     */
    public AccountForm()
    {
    }
    
    /**
     * Create a new instance of AccountForm.
     * 
     * @param firstName the user's first name
     * @param lastName the user's surname
     * @param email the user's email
     */
    public AccountForm(final String firstName, 
            final String lastName, 
            final String email)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * @return the firstName
     */
    public String getFirstName()
    {
        return firstName;
    }
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }
    /**
     * @return the lastName
     */
    public String getLastName()
    {
        return lastName;
    }
    /**
     * @param lastName the lastName to set
     */
    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }
    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }
    /**
     * @param email the email to set
     */
    public void setEmail(final String email)
    {
        this.email = email;
    }
    

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this,
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
