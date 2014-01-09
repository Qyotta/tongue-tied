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
package org.tonguetied.datatransfer.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.Translation;


/**
 * Interface defining Keyword DAO facade for TongueTied storage.
 * 
 * @author bsion
 */
public interface TransferRepository 
{
    /**
     * Persist a {@link Keyword} to permanent storage.
     * 
     * @param keyword the keyword item to save or update.
     * @throws DataAccessException if the operation fails.
     */
    void saveOrUpdate(Keyword keyword) throws DataAccessException;
    
    /**
     * Execute a report query to locate all {@link Translation}s that match the
     * export criteria specified in the {@link ExportParameters}.
     * 
     * @param parameters set of attributes used to filter result set
     * @return all {@link Translation}s that match the search criteria.
     */
    List<Translation> findTranslations(ExportParameters parameters);
}
