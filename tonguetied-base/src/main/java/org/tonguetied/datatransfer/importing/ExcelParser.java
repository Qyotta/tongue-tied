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

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.tonguetied.datatransfer.importing.ImportException.ImportErrorCode;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.Translation;

/**
 * Parser used for parsing an excel spreadsheet and then generating a map of
 * {@link Keyword}s. All spreadsheet cells are parsed and a map of 
 * {@link Keyword}s with {@link Translation}s are built. This {@linkplain Map} 
 * can then be processed by the calling importer.
 * 
 * @author bsion
 *
 */
public interface ExcelParser extends HSSFListener
{
    /**
     * @return map of {@link Keyword}s generated from the parsing, using the 
     * keyword as the map key. An empty map should be returned if no 
     * {@linkplain Keyword}s were generated
     */
    Map<String, Keyword> getKeywords();
    
    /**
     * Return the list {@link ImportErrorCode}s generated during the 
     * parsing of the excel file.
     * 
     * @return a list of any {@link ImportErrorCode}s. The list may be empty
     */
    List<ImportErrorCode> getErrorCodes();
}
