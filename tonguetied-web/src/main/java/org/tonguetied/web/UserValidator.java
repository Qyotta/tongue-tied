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

import static org.tonguetied.web.UserForm.FIELD_USER_EMAIL;
import static org.tonguetied.web.UserForm.FIELD_USER_FIRSTNAME;
import static org.tonguetied.web.UserForm.FIELD_USER_LASTNAME;
import static org.tonguetied.web.UserForm.FIELD_USER_PASSWORD;
import static org.tonguetied.web.UserForm.FIELD_USER_REPEATED_PASSWORD;
import static org.tonguetied.web.UserForm.FIELD_USER_USERNAME;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.tonguetied.usermanagement.User;
import org.tonguetied.usermanagement.UserService;

/**
 * Validator for the {@link User} object.
 * 
 * @author bsion
 * 
 */
public class UserValidator implements Validator
{
    private UserService userService;

    public boolean supports(Class clazz)
    {
        return UserForm.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors)
    {
        UserForm userForm = (UserForm) target;
        validateMandatoryFields(userForm.getUser(), errors);
        validateDuplicates(userForm.getUser(), errors);
        validateEmail(userForm.getUser().getEmail(), errors);
        validatePassword(userForm.getUser(), errors);
    }

    /**
     * This validation method checks if the password and re-entered password
     * match and confirm to a valid format.
     * 
     * @param user the {@link User} object to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validatePassword(final User user, Errors errors)
    {
        // check for duplicates of new records only
        if (user.getId() == null)
        {
            ValidationUtils.rejectIfEmpty(errors, FIELD_USER_PASSWORD,
                    "error.password.required");
            if (user.getPassword() != null)
            {
                if (!user.getPassword().equals(user.getRepeatedPassword()))
                    errors.rejectValue(FIELD_USER_REPEATED_PASSWORD, "error.password.mismatch");
            }
        }
    }

    /**
     * This validation method check if the all mandatory fields on a
     * {@link User} object have been set.
     * 
     * @param user the {@link User} object to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validateMandatoryFields(final User user, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIELD_USER_USERNAME,
                "error.username.required", null, "default");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIELD_USER_FIRSTNAME,
                "error.first.name.required", null, "default");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIELD_USER_LASTNAME,
                "error.last.name.required", null, "default");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIELD_USER_EMAIL,
                "error.email.required", null, "default");
    }

    /**
     * This validation method checks if a {@link User} object already exists in
     * persistence with the same business key ({@link User#getUserName()}).
     * 
     * @param user the {@link User} object to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validateDuplicates(final User user, Errors errors)
    {
        // check for duplicates of new records only
        if (user.getId() == null)
        {
            User other = userService.getUser(user.getUsername());
            if (other != null)
            {
                errors.rejectValue(FIELD_USER_USERNAME, "error.user.already.exists",
                        new String[] { user.getUsername() }, "default");
            }
        }
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
            errors.rejectValue(FIELD_USER_EMAIL, "error.invalid.email");
        }
    }

    /**
     * Set the {@link UserService} instance.
     * 
     * @param userService the {@link UserService} instance.
     */
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }
}
