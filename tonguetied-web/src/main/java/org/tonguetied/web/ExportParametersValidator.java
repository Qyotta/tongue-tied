/*
 * Copyright 2008 The Tongue-Tied Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.tonguetied.web;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.tonguetied.datatransfer.common.ExportParameters;

/**
 * Validator for the {@link ExportParameters} object.
 * 
 * @author bsion
 * 
 */
public class ExportParametersValidator implements Validator {

    static final String FIELD_COUNTRIES = "countries";
    static final String FIELD_BUNDLES = "bundles";
    static final String FIELD_LANGUAGES = "languages";
    static final String FIELD_FORMAT_TYPE = "formatType";
    static final String FIELD_TRANSLATION_STATE = "translationState";

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(Class klass) {
        return ExportParameters.class.isAssignableFrom(klass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.validation.Validator#validate(java.lang.Object,
     * org.springframework.validation.Errors)
     */
    public void validate(Object target, Errors errors) {
        ExportParameters parameters = (ExportParameters) target;
        if (parameters.getFormatType() == null) {
            errors.rejectValue(FIELD_FORMAT_TYPE, "error.export.type.required");
        }
        if (CollectionUtils.isEmpty(parameters.getTranslationStates())) {
            errors.rejectValue(FIELD_TRANSLATION_STATE, "error.translation.state.selection");
        }
        if (CollectionUtils.isEmpty(parameters.getLanguages())) {
            errors.rejectValue(FIELD_LANGUAGES, "error.language.selection");
        }
        if (CollectionUtils.isEmpty(parameters.getBundles())) {
            errors.rejectValue(FIELD_BUNDLES, "error.bundle.selection");
        }
        if (CollectionUtils.isEmpty(parameters.getCountries())) {
            errors.rejectValue(FIELD_COUNTRIES, "error.country.selection");
        }
    }
}
