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
package org.tonguetied.datatransfer.common;

/**
 * The type of output format the results of the export query should take.
 * Exported data will be put into a file format commensurate with the export
 * type.
 * 
 * @author bsion
 * 
 */
public enum FormatType {

    /**
     * Java properties file
     */
    JAVA_PROPERTIES("properties"),

    /**
     * Microsoft national language support file
     */
    NLS_PROPERTIES("nls"),
    /**
     * Comma separated file type
     */
    CSV("csv"),

    /**
     * Excel spreadsheet
     */
    XLS("xls"),

    /**
     * Excel spreadsheet in a language centric view
     */
    XLS_LANGUAGE("xls");

    private final String fileExtension;

    private FormatType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    /**
     * 
     * @return the file extension used to identify files of this type
     */
    public String getFileExtension() {
        return fileExtension;
    }
}