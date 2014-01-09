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
package org.tonguetied.web;

import org.apache.commons.collections.Predicate;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;

/**
 * Filter used to determine if a {@link Translation} matches the preferences set
 * in the {@link PreferenceForm}.
 * 
 * @author bsion
 * 
 */
public class PreferenceFilter implements Predicate
{

    private PreferenceForm preferences;

    /**
     * Create a new instance of the <code>PreferenceFilter</code>.
     * 
     * @param preferences the {@link PreferenceForm} used to evaluate if a
     *        {@link Translation} should be filter away or not
     */
    public PreferenceFilter(PreferenceForm preferences)
    {
        this.preferences = preferences;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object object)
    {
        Translation translation = (Translation) object;

        boolean result = false;

        if (translation != null)
        {
            result = isValidCounty(translation.getCountry())
                    && isValidBundle(translation.getBundle())
                    && isValidLanguage(translation.getLanguage());
        }

        return result;
    }

    /**
     * Determine if the bundle of a {@link Translation} is in the list of
     * {@link Bundle}s. If the bundle is <code>null</code> then returns
     * <code>true</code>. If there are no selected bundles, then 
     * <code>true</code> is returned.
     * 
     * @param bundle the Bundle of the translation to evaluate
     * @return <code>true</code> if the bundle is valid, <code>false</code>
     *         otherwise
     */
    private boolean isValidBundle(final Bundle bundle)
    {
        boolean result = false;

        if (bundle == null)
        {
            result = true;
        }
        else if (preferences.getSelectedBundles() == null || 
                preferences.getSelectedBundles().isEmpty())
        {
            result = true;
        }
        else
        {
            result = preferences.getSelectedBundles().contains(bundle);
        }

        return result;
    }
    
    /**
     * Determine if the country of a {@link Translation} is in the list of
     * {@link Country}s. If the country is <code>null</code> then returns
     * <code>true</code>. If there are no selected countries, then 
     * <code>true</code> is returned.
     * 
     * @param country the Country of the translation to evaluate
     * @return <code>true</code> if the country is valid, <code>false</code>
     *         otherwise
     */
    private boolean isValidCounty(final Country country)
    {
        boolean result = false;
        if (country == null)
        {
            result = true;
        }
        else if (preferences.getSelectedCountries() == null ||
                preferences.getSelectedCountries().isEmpty())
        {
            result = true;
        }
        else
        {
            result = preferences.getSelectedCountries().contains(country);
        }
        
        return result;
    }
    
    /**
     * Determine if the language of a {@link Translation} is in the list of
     * {@link Language}s. If the bundle is <code>null</code> then returns
     * <code>true</code>. If there are no selected languages, then 
     * <code>true</code> is returned.
     * 
     * @param language the Language of the translation to evaluate
     * @return <code>true</code> if the language is valid, <code>false</code>
     *         otherwise
     */
    private boolean isValidLanguage(final Language language)
    {
        boolean result = false;

        if (language == null)
        {
            result = true;
        }
        else if (preferences.getSelectedLanguages() == null ||
                preferences.getSelectedLanguages().isEmpty())
        {
            result = true;
        }
        else
        {
            result = preferences.getSelectedLanguages().contains(language);
        }

        return result;
    }
}
