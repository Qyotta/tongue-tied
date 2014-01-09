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
package org.tonguetied.keywordmanagement;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.tonguetied.utils.pagination.Order;
import org.tonguetied.utils.pagination.PaginatedList;


/**
 * Interface defining the DAO facade for the Keyword aggregation in TongueTied 
 * storage.
 * 
 * @author bsion
 */
public interface KeywordRepository 
{
    /**
     * Persist a {@link Keyword} to permanent storage.
     * 
     * @param keyword the keyword item to save or update.
     * @throws DataAccessException if the operation fails.
     */
    void saveOrUpdate(Keyword keyword) throws DataAccessException;
    
    /**
     * Remove an {@link Keyword} from permanent storage.
     * 
     * @param keyword the keyword item to remove.
     */
    void delete(Keyword keyword);

    /**
     * Retrieve the {@link Keyword} from permanent storage.
     * 
     * @param id the unique identifier of the {@link Keyword}.
     * @return the {@link Keyword} matching the <code>id</code> or 
     * <code>null<code> if no match is found.
     */
    Keyword getKeyword(final Long id);
    
    /**
     * Retrieve the {@link Keyword} from permanent storage.
     * 
     * @param keywordString the unique name of the {@link Keyword}.
     * @return the {@link Keyword} matching the <code>keywordString</code> or 
     * <code>null<code> if no match is found.
     */
    Keyword getKeyword(final String keywordString);
    
    /**
     * Retrieve all {@link Keyword}s from permanent storage with page support.
     * 
     * @param firstResult a row number, numbered from 0. If <code>null</code>
     * then then results begin at zero. If the value is negative, then the 
     * results begin at zero
     * @param maxResults the maximum number of rows. If <code>null</code> then
     * all results are returned
     * @param order indicates the order the results should be returned
     * @return the {@link List} of all {@link Keyword}s in the system.
     */
    PaginatedList<Keyword> getKeywords(final Integer firstResult,
                              final Integer maxResults, final Order order);

    /**
     * Find all {@link Keyword}s whose matching the search criteria.
     * 
     * @param keyword the criteria to search for
     * @param ignoreCase <code>true</code> if case should be ignored. 
     * <code>false</code> otherwise
     * @param order indicates the order the results should be returned
     * @param firstResult a row number, numbered from 0. If <code>null</code>
     * then then results begin at zero
     * @param maxResults the maximum number of rows. If <code>null</code> then
     * all results are returned
     * @return a list of all keywords matching the criteria
     * @throws IllegalArgumentException if the keyword is <code>null</code>
     * @throws IllegalArgumentException if the matchMode is <code>null</code>
     */
    PaginatedList<Keyword> findKeywords(Keyword keyword, 
                               final boolean ignoreCase,
                               final Order order,
                               final Integer firstResult,
                               final Integer maxResults)
            throws IllegalArgumentException;
    
    /**
     * Determines if there are any translations that reference this objects 
     * passed in.
     * 
     * @param propertyName the name of the property to check
     * @param value the value to of the property to check
     * @return the total number of references to the input object
     */
    int getReferences(final String propertyName, final Object value);
}
