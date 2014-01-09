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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Value object used to pass the parameters for a change password request by a
 * user.
 * 
 * @author bsion
 *
 */
public class ChangePasswordForm
{
    private String oldPassword;
    private String newPassword;
    private String newRepeatedPassword;
    private long userId;
    
    /**
     * @return the userId
     */
    public long getUserId()
    {
        return userId;
    }

    /**
     * Assign the userId.
     *
     * @param userId the userId to set
     */
    public void setUserId(final long userId)
    {
        this.userId = userId;
    }

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the newRepeatedPassword
     */
    public String getNewRepeatedPassword() {
        return newRepeatedPassword;
    }

    /**
     * @param newRepeatedPassword the newRepeatedPassword to set
     */
    public void setNewRepeatedPassword(String newRepeatedPassword) {
        this.newRepeatedPassword = newRepeatedPassword;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
            append("userId", userId).
            append("password", "********").
            toString();
    }

}
