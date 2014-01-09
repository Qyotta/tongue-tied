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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class contain common web side validation methods.
 * 
 * @author bsion
 *
 */
public final class WebValidationUtils
{
    /**
     * The regular expression string used to evaluate strings.
     */
    private static final String EMAIL_REGEX_EXPRESSION = ".+@.+\\.[a-z]+";
    
    /**
     * the email pattern string
     */
    private static final Pattern emailPattern = 
        Pattern.compile(EMAIL_REGEX_EXPRESSION);

    /**
     * Check if the string provided is in a valid email format. The basic 
     * format is "xxx@yyy.zzz". <code>null</code> values are considered
     * invalid.
     *  
     * @param email the email string to validate
     * @return <code>true</code> if the email is in valid format,
     * <code>false</code> otherwise
     */
    public static final boolean isEmailValid(final String email)
    {
        boolean result = false;
        if (email != null) {
            //Match the given string with the pattern
            final Matcher m = emailPattern.matcher(email);
            result = m.matches();
        }
        
        return result;
    }
}
