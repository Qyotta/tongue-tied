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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Utility class of common methods for request evaluation.
 * 
 * @author bsion
 *
 */
public final class RequestUtils
{

    private static final String REQUEST_METHOD_GET = "GET";

    /**
     * Evaluate if the request is a GET method
     * 
     * @param request the HTTP request to evaluate
     * 
     * @return <code>true</code> if the request method is a GET Request, 
     * <code>false</code> otherwise
     */
    static boolean isGetMethod(final HttpServletRequest request)
    {
        return REQUEST_METHOD_GET.equalsIgnoreCase(request.getMethod());
    }
    
    /**
     * Get the value of the parameter as a Long value.
     * 
     * @param request the current request
     * @param name the name of the parameter
     * @return the Long value of the parameter, or <code>null</code> if it is
     * not present
     * @throws NumberFormatException if the value is not a valid Long
     */
    static Long getLongParameter(final HttpServletRequest request,
            final String name)
    {
        final String stringValue = request.getParameter(name);
        Long value = null;
        if (StringUtils.isNotBlank(stringValue))
        {
            value = Long.parseLong(stringValue);
        }
        
        return value;
    }

    /**
     * Get the value of the parameter as a Boolean value.
     * 
     * @param request the current request
     * @param name the name of the parameter
     * @return the Boolean value of the parameter, or <code>null</code> if it 
     * is not present
     * @throws NumberFormatException if the value is not a valid Boolean
     */
    static Boolean getBooleanParameter(final HttpServletRequest request,
            final String name)
    {
        final String stringValue = request.getParameter(name);
        Boolean value = null;
        if (StringUtils.isNotBlank(stringValue))
        {
            value = Boolean.valueOf(stringValue);
        }
        
        return value;
    }
    
    /**
     * Get the value of the parameter as a Integer value.
     * 
     * @param request the current request
     * @param name the name of the parameter
     * @return the Integer value of the parameter, or <code>null</code> if it 
     * is not present
     * @throws NumberFormatException if the value is not a valid Integer
     */
    static Integer getIntegerParameter(final HttpServletRequest request,
            final String name)
    {
        final String stringValue = request.getParameter(name);
        Integer value = null;
        if (StringUtils.isNotBlank(stringValue))
        {
            value = Integer.valueOf(stringValue);
        }
        
        return value;
    }
}
