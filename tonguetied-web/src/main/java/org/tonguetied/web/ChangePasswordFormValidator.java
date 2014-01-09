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

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for the {@link ChangePasswordForm}.
 * 
 * @author bsion
 * 
 */
public class ChangePasswordFormValidator implements Validator
{
    static final String FIELD_NEW_PASSWORD = "newPassword";
    static final String FIELD_NEW_REPEATED_PASSWORD = "newRepeatedPassword";

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class klass)
    {
        return ChangePasswordForm.class.isAssignableFrom(klass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     *      org.springframework.validation.Errors)
     */
    public void validate(Object target, Errors errors)
    {
        ValidationUtils.rejectIfEmpty(errors, FIELD_NEW_PASSWORD,
                "error.password.required");
        validateNewPassword((ChangePasswordForm) target, errors);
    }

    /**
     * Checks that the retyped new password matches the first value.
     * 
     * @param form the {@link ChangePasswordForm} to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validateNewPassword(final ChangePasswordForm form,
            Errors errors)
    {
        if (form.getNewPassword() != null)
        {
            if (!form.getNewPassword().equals(form.getNewRepeatedPassword()))
                errors.rejectValue(FIELD_NEW_REPEATED_PASSWORD,
                        "error.password.mismatch");
        }
    }
}
