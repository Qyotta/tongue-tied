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

import static org.tonguetied.web.Constants.BUNDLES;
import static org.tonguetied.web.Constants.FORMAT_TYPES;
import static org.tonguetied.web.Constants.STATES;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.tonguetied.datatransfer.DataService;
import org.tonguetied.datatransfer.common.FormatType;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Translation.TranslationState;


/**
 * Manage the submission of import requests. This controller is responsible 
 * for importing in new or existing keywords to the system.
 * 
 * @author bsion
 *
 */
public class ImportController extends CancellableFormController
{
    private DataService dataService;
    private KeywordService keywordService;
    
    private static final Logger logger = 
        Logger.getLogger(ImportController.class);

    /**
     * Create new instance of CountryController 
     */
    public ImportController()
    {
        setCommandClass(ImportBean.class);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) 
            throws Exception
    {
        return new ImportBean();
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    Object command,
                                    BindException errors) throws Exception
    {
        if (logger.isDebugEnabled()) logger.debug("beginning import");
        // cast the bean
        ImportBean bean = (ImportBean) command;

        // let's see if there's content there
        MultipartFile file = bean.getFileUploadBean().getFile();
        if (file != null && !file.isEmpty())
        {
            bean.getParameters().setFileName(FilenameUtils.getBaseName(file.getOriginalFilename()));
            bean.getParameters().setData(file.getBytes());
            dataService.importData(bean.getParameters());
        }
        else
        {
            // hmm, that's strange, the user did not upload anything
        }

        return new ModelAndView(getSuccessView());
    }

    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object command) throws Exception 
    {
        return new ModelAndView(getCancelView());
    }
    
    @Override
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) 
            throws Exception 
    {
        // to actually be able to convert Multipart instance to byte[]
        // we have to register a custom editor
        binder.registerCustomEditor(byte[].class, 
                new ByteArrayMultipartFileEditor());
        binder.registerCustomEditor(FormatType.class, new FormatTypeSupport());
        binder.registerCustomEditor(TranslationState.class, 
                new TranslationStateSupport()); 
        binder.registerCustomEditor(Bundle.class, 
                new BundleSupport(keywordService.getBundles()));
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) 
            throws Exception 
    {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(FORMAT_TYPES, FormatType.values());
        model.put(STATES, TranslationState.values());
        model.put(BUNDLES, keywordService.getBundles());
        
        return model;
    }

    /**
     * Assign the {@link DataService}.
     * 
     * @param dataService the {@link DataService} to set.
     */
    public void setDataService(final DataService dataService)
    {
        this.dataService = dataService;
    }

    /**
     * Assign the {@link KeywordService}
     * 
     * @param keywordService the keywordService to set
     */
    public void setKeywordService(KeywordService keywordService)
    {
        this.keywordService = keywordService;
    }
}
