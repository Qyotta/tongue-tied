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

import java.io.File;
import java.util.Date;

/**
 * Value object used to hold the contents of a directory or file and used for
 * display.
 *  
 * @author bsion
 *
 */
public class FileBean extends File
{
    private static final long serialVersionUID = -2248589294097284873L;
    private String suffix;

    /**
     * Create a new instance of FileBean.
     * 
     * @param pathName the string location of the path
     */
    public FileBean(final String pathName)
    {
        super(pathName);
        setSuffix();
    }
    
    /**
     * Create a new instance of FileBean.
     * 
     * @param file The parent abstract pathname
     */
    public FileBean(final File file)
    {
        super(file, "");
        setSuffix();
    }
    
    /**
     * Java bean accessor method for view technology.
     * 
     * @see File#length()
     */
    public long getSize()
    {
        return super.length();
    }
    
    /**
     * Java bean accessor method for view technology. If the directory does not
     * contain any files then an empty array is returned.
     * 
     * @see File#listFiles()
     * @return an array of FileBeans listing the contents of this directory
     */
    public FileBean[] getFiles()
    {
        final File[] files = super.listFiles();
        FileBean[] fileBeans;
        if (files != null)
        {
            fileBeans = new FileBean[files.length];
            for (int i=0; i<files.length; i++)
            {
                fileBeans[i] = new FileBean(files[i]);
            }
        }
        else
        {
            fileBeans = new FileBean[0];
        }
        
        return fileBeans;
    }
    
    /**
     * Java bean accessor method for view technology.
     * 
     * @return the date that file was last modified
     */
    public Date getLastModifiedDate()
    {
        return new Date(super.lastModified());
    }
    
    /**
     * Return the type of this file based off the suffix.
     * 
     * @return string indicating the type of this file
     */
    public String getFileType()
    {
        return suffix;
    }
    
    /**
     * Determine the suffix of the file if there is one and set its value.
     */
    private void setSuffix() 
    {
        final String name = super.getName();
        int suffixIndex = name.lastIndexOf(".");
        if ((suffixIndex > 0) && ((suffixIndex+1) < name.length()))
        {
            suffix = name.substring(suffixIndex+1);
        }
        else
        {
            suffix = null;
        }
    }
}
