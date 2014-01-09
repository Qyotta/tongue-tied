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
package org.tonguetied.datatransfer.exporting;

/**
 * Exception thrown when an error occurs during a data export. This exception
 * acts as wrapper to the underlying cause, which could be from when data is
 * extracted from persistence or during the formatting of that data into the 
 * appropriate format.
 * 
 * @author bsion
 *
 */
public class ExportException extends RuntimeException
{

    private static final long serialVersionUID = 9062827618835803948L;

    /**
     * Create a new instance of ExportException.
     */
    public ExportException() {
    }

    /**
     * Create a new instance of ExportException.
     * 
     * @param message the message detail
     */
    public ExportException(String message) {
        super(message);
    }

    /**
     * Create a new instance of ExportException.
     * 
     * @param cause the root cause
     */
    public ExportException(Throwable cause) {
        super(cause);
    }

    /**
     * Create a new instance of ExportException.
     * 
     * @param message the message detail
     * @param cause the root cause
     */
    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
