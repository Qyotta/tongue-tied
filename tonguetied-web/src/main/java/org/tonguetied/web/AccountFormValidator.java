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

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Validator for the {@link AccountForm} object.
 * 
 * @author bsion
 *
 */
public class AccountFormValidator implements Validator
{

    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class klass)
    {
        return AccountForm.class.isAssignableFrom(klass);
    }

    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object target, Errors errors)
    {
        final AccountForm form = (AccountForm) target;
        validateMandatoryFields(form, errors);
        validateEmail(form.getEmail(), errors);
    }

    /**
     * This validation method check if the all mandatory fields on a 
     * {@link AccountForm} object have been set.
     * 
     * @param form the {@link AccountForm} object to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validateMandatoryFields(final AccountForm form, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, FIELD_FIRSTNAME, "error.first.name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, FIELD_LASTNAME, "error.last.name.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, FIELD_EMAIL, "error.email.required");
    }
    
    /**
     * This validation method checks that an email string confirms to a basic
     * structure or format. The basic format is "xxx@yyy.zzz"
     *  
     * @param email the email string to validate
     * @param errors contextual state about the validation process (never null)
     * @see WebValidationUtils#isEmailValid(String)
     */
    private void validateEmail(final String email, Errors errors)
    {
        if (!WebValidationUtils.isEmailValid(email))
        {
            errors.rejectValue(FIELD_EMAIL, "error.invalid.email");
        }
    }
}
