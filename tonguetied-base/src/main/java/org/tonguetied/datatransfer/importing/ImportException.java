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
package org.tonguetied.datatransfer.importing;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception thrown when an error occurs during a data import. This exception
 * acts as wrapper to the underlying cause, which could be from when imput data
 * is parsed from the source file.
 * 
 * @author bsion
 * 
 */
public class ImportException extends RuntimeException
{
    private List<ImportErrorCode> errorCodes;

    private static final long serialVersionUID = -8338610645953970655L;

    /**
     * Create a new instance of ExportException.
     * 
     * @param errorCodes the list of codes for each error that occurred during
     *        the import process
     */
    public ImportException(List<ImportErrorCode> errorCodes)
    {
        this.errorCodes = errorCodes;
    }

    /**
     * Create a new instance of ExportException.
     * 
     * @param cause the root cause
     */
    public ImportException(Throwable cause)
    {
        this(cause.getMessage(), cause);
    }

    /**
     * Create a new instance of ExportException.
     * 
     * @param message the message detail
     * @param cause the root cause
     */
    public ImportException(String message, Throwable cause)
    {
        super(message, cause);
        this.errorCodes = new ArrayList<ImportErrorCode>();
    }

    /**
     * @return the errorCodes
     */
    public List<ImportErrorCode> getErrorCodes()
    {
        return errorCodes;
    }

    /**
     * The type of errors that can occur during the import process. Each value
     * describes the type of error that occurred making it easier to identify
     * problem files during import.
     * 
     * @author bsion
     * 
     */
    protected enum ImportErrorCode
    {
        emptyData, illegalCountry, illegalLanguage, illegalTranslationState, 
        invalidNameFormat, unknownBundle, unknownCountry, unknownLanguage
    }
}
