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
package org.tonguetied.usermanagement;

import org.springframework.dao.DataAccessException;
import org.tonguetied.utils.pagination.PaginatedList;


/**
 * Interface defining User DAO facade for TongueTied storage.
 * 
 * @author mforslund
 */
public interface UserRepository 
{
    /**
     * Persist a {@link User} object to permanent storage.
     * 
     * @param user the user to save or update.
     * @throws DataAccessException if the operation fails.
     */
    void saveOrUpdate(User user) throws DataAccessException;
    
    /**
     * Remove a {@link User} object from permanent storage.
     * 
     * @param user the item to remove.
     */
    void delete(User user);

    /**
     * Retrieve the user by business key, ie the {@link User#getUsername()} 
     * from permanent storage.
     * 
     * @param username the business key to search for
     * @return the {@link User} matching the <code>username</code> or 
     * <code>null</code> if no match is found
     */
    User getUser(final String username);
    
    /**
     * Retrieve the user by the unique identifier from permanent storage.
     * 
     * @param id the unique key to search for
     * @return the {@link User} matching the <code>id</code> or 
     * <code>null</code> if no match is found
     */
    User getUser(final Long id);
    
    /**
     * Retrieve a set of all {@link User}s from permanent storage with page
     * support.
     * 
     * @param firstResult a row number, numbered from 0. If <code>null</code>
     * then then results begin at zero. If the value is negative, then the 
     * results begin at zero
     * @param maxResults the maximum number of rows. If <code>null</code> then
     * all results are returned
     * @return a set of all {@link User}s in the system
     */
    PaginatedList<User> getUsers(final Integer firstResult,
            final Integer maxResults);
    
    /**
     * Retrieve all users matching the search criteria from permanent storage.
     * 
     * @param user the search criteria
     * @param firstResult a row number, numbered from 0. If <code>null</code>
     * then then results begin at zero
     * @param maxResults the maximum number of rows. If <code>null</code> then
     * all results are returned
     * @return the collection of {@link User}s matching the criteria
     */
    PaginatedList<User> findUsers(final User user, final Integer firstResult,
            final Integer maxResults);
}
