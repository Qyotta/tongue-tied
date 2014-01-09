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

import static org.tonguetied.keywordmanagement.Bundle.FIELD_NAME;
import static org.tonguetied.keywordmanagement.Bundle.FIELD_RESOURCE_NAME;
import static org.tonguetied.web.Constants.FILE_SEPARATOR;
import static org.tonguetied.web.Constants.FORM_FEED;
import static org.tonguetied.web.Constants.GROUP_SEPARATOR;
import static org.tonguetied.web.Constants.HORIZONTAL_TABULATION;
import static org.tonguetied.web.Constants.LINE_SEPARATOR;
import static org.tonguetied.web.Constants.PARAGRAPH_SEPARATOR;
import static org.tonguetied.web.Constants.RECORD_SEPARATOR;
import static org.tonguetied.web.Constants.SPACE_SEPARATOR;
import static org.tonguetied.web.Constants.UNIT_SEPARATOR;
import static org.tonguetied.web.Constants.VERTICAL_TABULATION;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.KeywordService;

/**
 * Validator for the {@link Bundle} object.
 * 
 * @author bsion
 * 
 */
public class BundleValidator implements Validator
{
    private KeywordService keywordService;

    private static final char[] WHITESPACE_CHARS = new char[] {
            SPACE_SEPARATOR, LINE_SEPARATOR, PARAGRAPH_SEPARATOR,
            HORIZONTAL_TABULATION, VERTICAL_TABULATION, FORM_FEED,
            FILE_SEPARATOR, GROUP_SEPARATOR, RECORD_SEPARATOR, UNIT_SEPARATOR,
            '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '+',
            '=', '|', '{', '}', '[', ']', ':', ';', '"', '\'', ',', '<', '>',
            '?'};
            
    /**
     * The regular expression string used to evaluate strings.
     */
//    private static final String RESOURCE_REGEX_EXPRESSION = ".+@.+\\.[a-z]+";
//    private static final String RESOURCE_REGEX_EXPRESSION = "[^@\\?]";
//    private static final String RESOURCE_REGEX_EXPRESSION = ".+[\\S]+[\\w]+";
    
    /**
     * the resource name pattern string
     */
//    private static final Pattern resourceNamePattern = 
//        Pattern.compile(RESOURCE_REGEX_EXPRESSION);

    public boolean supports(Class clazz)
    {
        return Bundle.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors)
    {
        validateMandatoryFields((Bundle) target, errors);
        validateCharacterSet((Bundle) target, errors);
        validateDuplicates((Bundle) target, errors);
    }

    /**
     * This validation method check if the all mandatory fields on a
     * {@link Bundle} object have been set.
     * 
     * @param bundle the {@link Bundle} object to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validateMandatoryFields(final Bundle bundle, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIELD_NAME,
                "error.bundle.name.required", null, "default");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIELD_RESOURCE_NAME,
                "error.bundle.resource.name.required", null, "default");
    }

    /**
     * This validation method checks if a {@link Bundle} object already exists
     * in persistence with the same business key ({@link Bundle#getName()}).
     * 
     * @param bundle the {@link Bundle} object to validate
     * @param errors contextual state about the validation process (never null)
     */
    private void validateDuplicates(final Bundle bundle, Errors errors)
    {
        List<Bundle> others = 
            keywordService.findBundles(bundle.getName(), bundle.getResourceName());
        if (!others.isEmpty())
        {
            for (Bundle other : others)
            {
                // Duplicates can be new bundles replicating existing bundles,
                // or updating an existing bundles code to another preexisting code
                if ((bundle.getId() == null) || (!bundle.getId().equals(other.getId())))
                {
                    if (bundle.getName().equals(other.getName()))
                        errors.rejectValue(FIELD_NAME, "error.bundle.already.exists",
                                new String[] { bundle.getName() },
                                "a bundle with this name already exists");
                    if (bundle.getResourceName().equals(other.getResourceName()))
                        errors.rejectValue(FIELD_RESOURCE_NAME,
                                "error.bundle.already.exists", 
                                new String[] { bundle.getResourceName() },
                                "a bundle with this resource name already exists");
                }
            }
        }
    }

    /**
     * This validation method checks if the bundle resource name contains any
     * invalid characters.
     * 
     * @param bundle the {@link Bundle} object to validate
     * @param errors contextual state about the validation process (never null)
     * @see Character#isWhitespace(char)
     */
    private void validateCharacterSet(final Bundle bundle, Errors errors)
    {
        //TODO: use a regular expression for this.
        if (StringUtils.containsAny(bundle.getResourceName(), WHITESPACE_CHARS))
        {
            errors.rejectValue(FIELD_RESOURCE_NAME,
                    "error.resource.name.contains.invalid.char",
                    new String[] { bundle.getResourceName() }, "default");
        }
//        if (bundle.getResourceName() != null)
//        {
//            final Matcher m = 
//                resourceNamePattern.matcher(bundle.getResourceName());
//            if (m.matches())
//            {
//                errors.rejectValue(FIELD_RESOURCE_NAME,
//                        "error.resource.name.contains.invalid.char",
//                        new String[] { bundle.getResourceName() }, "default");
//            }
//        }
    }

    /**
     * Set the {@link KeywordService} instance.
     * 
     * @param keywordService the keywordService instance.
     */
    public void setKeywordService(KeywordService keywordService)
    {
        this.keywordService = keywordService;
    }
}
