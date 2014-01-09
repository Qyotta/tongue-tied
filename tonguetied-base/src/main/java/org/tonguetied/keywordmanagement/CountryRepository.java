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
import org.tonguetied.keywordmanagement.Country.CountryCode;


/**
 * Interface defining the DAO facade for the Country in TongueTied storage.
 * 
 * @author bsion
 */
public interface CountryRepository 
{
    /**
     * Persist an <code>Country</code> to permanent storage.
     * 
     * @param country the item to save or update.
     * @throws DataAccessException if the operation fails.
     */
    void saveOrUpdate(Country country) throws DataAccessException;
    
    /**
     * Remove a <code>Country</code> from permanent storage.
     * 
     * @param country the item to remove.
     */
    void delete(Country country);

    /**
     * Retrieve the {@link Country} from permanent storage.
     * 
     * @param id the unique identifier of the {@link Country}.
     * @return the {@link Country} matching the <code>id</code> or 
     * <code>null<code> if no match is found.
     */
    Country getCountry(final Long id);

    /**
     * Retrieve the {@link Country} from permanent storage.
     * 
     * @param code the unique {@link CountryCode} identifying the {@link Country}.
     * @return the {@link Country} matching the <code>id</code> or 
     * <code>null<code> if no match is found.
     */
    Country getCountry(final CountryCode code);
    
    /**
     * Retrieve all {@link Country}s from permanent storage.
     * 
     * @return the {@link List} of all {@link Country}s in the system.
     */
    List<Country> getCountries();
}
