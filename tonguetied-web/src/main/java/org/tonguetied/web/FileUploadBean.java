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

import org.springframework.web.multipart.MultipartFile;


/**
 * Value object used to hold the contents of an uploaded file received in a 
 * multipart request.
 * 
 * @author bsion
 *
 */
public class FileUploadBean {

    private MultipartFile file;

    /**
     * Create a new instance of FileUploadBean.
     */
    public FileUploadBean() {
    }

    /**
     * @return a representation of the uploaded file 
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * @param file the uploaded file representation
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
