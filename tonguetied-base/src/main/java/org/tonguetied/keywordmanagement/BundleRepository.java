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


/**
 * Interface defining DAO facade for TongueTied storage of the {@link Bundle}
 * object.
 * 
 * @author bsion
 */
public interface BundleRepository 
{
    /**
     * Persist a {@link Bundle} and if this bundle has been marked as the 
     * default bundle, then reset all other bundles so they are not the default.
     *   
     * @param bundle the bundle to save or update
     * @throws DataAccessException if the operation fails.
     */
    void saveOrUpdate(Bundle bundle) throws DataAccessException;
    
    /**
     * Remove a {@link Bundle} from permanent storage.
     * 
     * @param bundle the item to remove.
     */
    void delete(Bundle bundle);

    /**
     * Retrieve the {@link Bundle} from permanent storage.
     * 
     * @param id the unique identifier of the {@link Bundle}.
     * @return the {@link Bundle} matching the <code>id</code> or 
     * <code>null<code> if no match is found.
     */
    Bundle getBundle(final Long id);

    /**
     * Retrieve the {@link Bundle} from permanent storage.
     * 
     * @param name the unique name identifying the {@link Bundle}.
     * @return the {@link Bundle} matching the <code>name</code> or 
     * <code>null<code> if no match is found.
     */
    Bundle getBundleByName(final String name);
    
    /**
     * Retrieve the {@link Bundle} from permanent storage.
     * 
     * @param resourceName the unique name identifying the {@link Bundle}.
     * @return the {@link Bundle} matching the <code>name</code> or 
     * <code>null<code> if no match is found.
     */
    Bundle getBundleByResourceName(final String resourceName);
    
    /**
     * Retrieve the {@link Bundle} marked as the default from permanent 
     * storage.
     * 
     * @return the default {@link Bundle} or <code>null</code> if one is not set
     */
    Bundle getDefaultBundle();
    
    /**
     * Retrieve all {@link Bundle}s from permanent storage.
     * 
     * @return the {@link List} of all {@link Bundle}s in the system.
     */
    List<Bundle> getBundles();
    
    /**
     * Find all {@link Bundle}s matching the search criteria.
     * 
     * @param name the unique name identifying the {@link Bundle}.
     * @param resourceName the unique resource name identifying the {@link Bundle}.
     * @return a list of all bundles matching the criteria
     * @throws IllegalArgumentException if the keyword is <code>null</code>
     */
    List<Bundle> findBundles(final String name, final String resourceName)
        throws IllegalArgumentException;
}
