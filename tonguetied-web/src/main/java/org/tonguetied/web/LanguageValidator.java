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

import static org.tonguetied.keywordmanagement.Language.FIELD_CODE;
import static org.tonguetied.keywordmanagement.Language.FIELD_NAME;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Language.LanguageCode;


/**
 * Validator for the {@link Language} object.
 * 
 * @author bsion
 *
 */
public class LanguageValidator implements Validator {
    
    private KeywordService keywordService;

    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class clazz) {
        return Language.class.isAssignableFrom(clazz);
    }

    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
     */
    public void validate(Object target, Errors errors) {
        validateMandatoryFields((Language) target, errors);
        validateDuplicates((Language) target, errors);
    }
    
    /**
     * This validation method check if the all mandatory fields on a 
     * {@link Country} object have been set.
     * 
     * @param language the {@link Language} object to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validateMandatoryFields(final Language language, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, FIELD_NAME, "error.language.name.required", null, "default");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, FIELD_CODE, "error.language.code.required", null, "default");
    }
    
    /**
     * This validation method checks if a {@link Language} object already exists 
     * in persistence with the same business key ({@link LanguageCode}).
     * 
     * @param language the {@link Language} object to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validateDuplicates(final Language language, Errors errors)
    {
        final Language other = keywordService.getLanguage(language.getCode());
        if (other != null)
        {
            // Duplicates can be new languages replicating existing languages,
            // or updating an existing language code to another preexisting code
            if ((language.getId() == null) || (!language.getId().equals(other.getId())))
            {
                    errors.rejectValue(
                            FIELD_CODE, 
                            "error.language.already.exists",
                            new String[] {language.getCode().name()},
                            "the language already exists in the system");
            }
        }
    }

    /**
     * Set the {@link KeywordService} instance.
     * 
     * @param keywordService the keywordService instance.
     */
    public void setKeywordService(KeywordService keywordService) {
        this.keywordService = keywordService;
    }
}
