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
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.KeywordService;


/**
 * Manage the submission of {@link Bundle}s. This controller is responsible 
 * for either creating a new {@link Bundle} or saving an existing one.
 * 
 * @author bsion
 *
 */
public class BundleController extends CancellableFormController {
    
    private KeywordService keywordService;
    
    private static final Logger logger = 
        Logger.getLogger(BundleController.class);

    /**
     * Create new instance of BundleController 
     */
    public BundleController() {
        setCommandClass(Bundle.class);
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
     */
    @Override
    protected void initBinder(HttpServletRequest request, 
                              ServletRequestDataBinder binder) throws Exception
    {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) 
            throws Exception
    {
        final Long id = RequestUtils.getLongParameter(request, "bundleId");
        Bundle bundle = keywordService.getBundle(id);
        if (bundle == null)
            bundle = new Bundle();
        
        return bundle;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    Object command,
                                    BindException errors) throws Exception
    {
        if (logger.isDebugEnabled()) logger.debug("saving bundle");
        Bundle bundle = (Bundle) command;
        
        keywordService.saveOrUpdate(bundle);
        
        return new ModelAndView(getSuccessView());
    }

    /**
     * Do nothing and return user to the location of {@link #getCancelView()}
     */
    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object command) throws Exception
    {
        return new ModelAndView(getCancelView());
    }

    /**
     * Assign the {@link KeywordService}.
     * 
     * @param keywordService the {@link KeywordService} to set.
     */
    public void setKeywordService(KeywordService keywordService) {
        this.keywordService = keywordService;
    }
}
