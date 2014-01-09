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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Manages the display of a file system directory that is within the context of
 * this application.
 * 
 * @author bsion
 *
 */
public class DirectoryViewController extends AbstractController
{
    private String suffix;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response)
    {
        final StringBuilder servletPath = new StringBuilder(request.getServletPath());
        if (logger.isDebugEnabled())
            logger.debug("servletPath = " + servletPath);
        if (servletPath.toString().endsWith(suffix))
            servletPath.delete(servletPath.length()-suffix.length(),
                    servletPath.length());
        
        final String homePath = 
            request.getSession().getServletContext().getRealPath(servletPath.toString());
        if (logger.isDebugEnabled())
            logger.debug("homePath = " + homePath);
        
        final FileBean baseDirectory = new FileBean(homePath);
        if (logger.isInfoEnabled())
            logger.info("displaying contents of basedir = " + baseDirectory);
        final int lastIndex = servletPath.lastIndexOf("/");
        final String[] parents;
        if (lastIndex > 0)
            parents = servletPath.substring(1, lastIndex).split("/");
        else
            parents = new String[]{};
        
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("baseDir", baseDirectory);
        model.put("suffix", suffix);
        model.put("parents", parents);
        
        return new ModelAndView("export/fileListing", model);
    }

    /**
     * @param suffix the suffix to set
     */
    public void setSuffix(final String suffix)
    {
        this.suffix = suffix;
    }
}
