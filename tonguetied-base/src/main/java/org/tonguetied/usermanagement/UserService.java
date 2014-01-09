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

import java.util.List;

import org.tonguetied.utils.pagination.PaginatedList;


/**
 * This interface defines the events used for user management.
 * 
 * @author bsion
 *
 */
public interface UserService {

    /**
     * Create or update a new user in the system.
     * 
     * @param user the {@link User} to persist
     */
    void saveOrUpdate(User user);

    /**
     * Create or update a new user in the system.
     * 
     * @param user the {@link User} to persist
     * @param encodePassword flag indicating if the users password should be 
     * encoded
     */
    void saveOrUpdate(User user, boolean encodePassword);
    
    /**
     * Retrieve the user by business key, ie the {@link User#getUsername()}.
     * 
     * @param username the business key to search for
     * @return the {@link User} matching the <code>username</code> or 
     * <code>null</code> if no match is found
     */
    User getUser(final String username);
    
    /**
     * Retrieve the user by the unique identifier.
     * 
     * @param id the unique key to search for
     * @return the {@link User} matching the <code>id</code> or 
     * <code>null</code> if no match is found
     */
    User getUser(final Long id);
    
    /**
     * Retrieve a list of all {@link User}s.
     * 
     * @return a list of all {@link User}s in the system
     */
    List<User> getUsers();
    
    /**
     * Get all the {@link User}s in the system.
     * 
     * @param firstResult a row number, numbered from 0. If <code>null</code>
     * then then results begin at zero. If the value is negative, then the 
     * results begin at zero
     * @param maxResults the maximum number of rows. If <code>null</code> then
     * all results are returned
     * @return all {@link User}s in the system.
     */
    PaginatedList<User> getUsers(final Integer firstResult,
            final Integer maxResults);

    /**
     * Find all {@link User}s that match the criteria specified. This method
     * should allow wild card searches on the user attributes.
     * 
     * @param user the object holding the attributes of the search 
     * criteria
     * @param firstResult a row number, numbered from 0. If <code>null</code>
     * then then results begin at zero
     * @param maxResults the maximum number of rows. If <code>null</code> then
     * all results are returned
     * @return a collection of {@link User} objects matching the search 
     * criteria
     */
    PaginatedList<User> findUsers(final User user, final Integer firstResult,
            final Integer maxResults);
    
    /**
     * Encode the new password and persist the changes to the {@link User}.
     * 
     * @param user the user to update
     * @param oldPassword the old raw value of this User's password to be 
     * validated
     * @param newPassword the new raw value of the password
     * @throws AuthenticationException if an invalid <code>oldPassword</code>
     * is supplied
     * @throws IllegalArgumentException if the new password is <code>null</code>
     */
    void changePassword(User user, final String oldPassword, final String newPassword) 
        throws AuthenticationException;
}
