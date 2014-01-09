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

import org.tonguetied.datatransfer.common.ImportParameters;

/**
 * Value object used as the model for import data features in the web interface.
 * This object acts as a composite of other objects needed for import 
 * functionality. 
 * 
 * @author bsion
 *
 */
public class ImportBean {
    private FileUploadBean fileUploadBean;
    private ImportParameters parameters;

    /**
     * Create a new instance of ImportBean.
     */
    public ImportBean() {
        this.fileUploadBean = new FileUploadBean();
        this.parameters = new ImportParameters();
    }

    /**
     * @return the fileUploadBean
     */
    public FileUploadBean getFileUploadBean() {
        return fileUploadBean;
    }

    /**
     * @param fileUploadBean the fileUploadBean to set
     */
    public void setFileUploadBean(FileUploadBean fileUploadBean) {
        this.fileUploadBean = fileUploadBean;
    }

    /**
     * @return the parameters of the import
     */
    public ImportParameters getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(ImportParameters parameters) {
        this.parameters = parameters;
    }
}
