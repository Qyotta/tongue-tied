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

import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.utils.pagination.Order;
import org.tonguetied.utils.pagination.PaginatedList;

/**
 * Facade exposing business functions of the TongueTied.
 * 
 * @author bsion
 */
public interface KeywordService 
{
    /**
     * Get the {@link Keyword} matching the <code>id</code>.
     * 
     * @param id the unique identifier of the {@link Keyword}.
     * @return the {@link Keyword} matching the <code>id</code> or 
     * <code>null</code> if no match is found.
     */
    Keyword getKeyword(final Long id);
    
    /**
     * Get all the {@link Keyword}s in the system. This method applies no 
     * pagination.
     * 
     * @return all {@link Keyword}s in the system.
     */
    List<Keyword> getKeywords();
    
    /**
     * Get all the {@link Keyword}s in the system.
     * 
     * @param firstResult a row number, numbered from 0. If <code>null</code>
     * then then results begin at zero. If the value is negative, then the 
     * results begin at zero
     * @param maxResults the maximum number of rows. If <code>null</code> then
     * all results are returned
     * @param order indicates the order the results should be returned
     * @return all {@link Keyword}s in the system.
     */
    PaginatedList<Keyword> getKeywords(final Integer firstResult, 
            final Integer maxResults, final Order order);
    
    /**
     * Get the {@link Keyword} matching the <code>keywordString</code>.
     * 
     * @param keywordString the string uniquely identifying the {@link Keyword}
     * @return the {@link Keyword} matching the <code>keywordString</code> or 
     * <code>null</code> if no match is found.
     */
    Keyword getKeyword(final String keywordString);
    
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
     * @return a list of all {@link Keyword}s matching the criteria
     */
    PaginatedList<Keyword> findKeywords(Keyword keyword, 
                               final boolean ignoreCase,
                               final Order order,
                               final Integer firstResult,
                               final Integer maxResults);
    
    /**
     * Get the {@link Language} matching the <code>id</code>.
     * 
     * @param id the unique identifier of the {@link Language}.
     * @return the {@link Language} matching the <code>id</code> or 
     * <code>null</code> if no match is found.
     */
    Language getLanguage(final Long id);
    
    /**
     * Get all the {@link Language}s in the system.
     * 
     * @return all {@link Language}s in the system.
     */
    List<Language> getLanguages();

    /**
     * Get the {@link Language} matching the {@link LanguageCode}.
     * 
     * @param code the {@link LanguageCode} uniquely identifying the 
     * {@link Language}.
     * @return the {@link Language} matching the {@link LanguageCode} or 
     * <code>null</code> if no match is found.
     */
    Language getLanguage(final LanguageCode code);
    
    /**
     * Get the {@link Country} matching the <code>id</code>.
     * 
     * @param id the unique identifier of the {@link Country}.
     * @return the {@link Country} matching the <code>id</code> or 
     * <code>null</code> if no match is found.
     */
    Country getCountry(final Long id);
    
    /**
     * Get the {@link Country} matching the {@link CountryCode}.
     * 
     * @param code the {@link CountryCode} uniquely identifying the 
     * {@link Country}.
     * @return the {@link Country} matching the {@link CountryCode} or 
     * <code>null</code> if no match is found.
     */
    Country getCountry(final CountryCode code);
    
    /**
     * Get all the {@link Country}s in the system.
     * 
     * @return all {@link Country}s in the system.
     */
    List<Country> getCountries();
    
    /**
     * Get the {@link Bundle} matching the <code>id</code>.
     * 
     * @param id the unique identifier of the {@link Bundle}.
     * @return the {@link Bundle} matching the <code>id</code> or 
     * <code>null</code> if no match is found.
     */
    Bundle getBundle(final Long id);
    
    /**
     * Get the {@link Bundle} matching the <code>name</code>.
     * 
     * @param name the string uniquely identifying the {@link Bundle}
     * @return the {@link Bundle} matching the <code>name</code> or 
     * <code>null</code> if no match is found
     */
    Bundle getBundleByName(final String name);
    
    /**
     * Get the {@link Bundle} matching the <code>resourceName</code>.
     * 
     * @param resourceName the string uniquely identifying the {@link Bundle}
     * @return the {@link Bundle} matching the <code>name</code> or 
     * <code>null</code> if no match is found
     */
    Bundle getBundleByResourceName(final String resourceName);
    
    /**
     * Find and return the default {@link Bundle} if one exists.
     * 
     * @return the {@link Bundle} marked as the default or <code>null</code> if
     * no bundle is set as the default.  
     */
    Bundle getDefaultBundle();
    
    /**
     * Get all the {@link Bundle}s in the system.
     * 
     * @return all {@link Bundle}s in the system.
     */
    List<Bundle> getBundles();	
    
    /**
     * Find all {@link Bundle}s matching the search criteria.
     * 
     * @param name the unique name identifying the {@link Bundle}.
     * @param resourceName the unique resource name identifying the {@link Bundle}.
     * @return a list of all {@link Bundle}s matching the criteria
     */
    List<Bundle> findBundles(final String name, final String resourceName);
    
    /**
     * Save or update the <code>Keyword</code>.
     * 
     * @param keyword the {@linkplain Keyword} to be persisted.
     */
    void saveOrUpdate(Keyword keyword);

    /**
     * Save or update the <code>Country</code>.
     * 
     * @param country the {@linkplain Country} to be persisted.
     */
    void saveOrUpdate(Country country);

    /**
     * Save or update the <code>Language</code>.
     * 
     * @param language the {@linkplain Language} to be persisted.
     */
    void saveOrUpdate(Language language);

    /**
     * Save or update the <code>Bundle</code>.
     * 
     * @param bundle the {@linkplain Bundle} to be persisted.
     */
    void saveOrUpdate(Bundle bundle);
    
    /**
     * Remove the {@link Keyword} matching the <code>id</code>.
     * 
     * @param id the unique identifier of the {@link Keyword} to be removed.
     */
    void deleteKeyword(final Long id);

    /**
     * Remove the Keyword.
     * 
     * @param keyword the {@linkplain Keyword} to be removed.
     */
    void delete(Keyword keyword);
    
    /**
     * Remove the {@link Country} matching the <code>id</code>. If other 
     * objects reference this object, then the object is not removed from 
     * persistence.
     * 
     * @param id the unique identifier of the {@link Country} to be removed.
     * @throws ReferenceException thrown if other objects have references to 
     * this object
     */
    void deleteCountry(final Long id);

    /**
     * Remove the {@link Language} matching the <code>id</code>. If other 
     * objects reference this object, then the object is not removed from 
     * persistence.
     * 
     * @param id the unique identifier of the {@link Language} to be removed.
     * @throws ReferenceException thrown if other objects have references to 
     * this object
     */
    void deleteLanguage(final Long id);

    /**
     * Remove the {@link Bundle} matching the <code>id</code>. If other 
     * objects reference this object, then the object is not removed from 
     * persistence.
     * 
     * @param id the unique identifier of the {@link Bundle} to be removed.
     * @throws ReferenceException thrown if other objects have references to 
     * this object
     */
    void deleteBundle(final Long id);
}
