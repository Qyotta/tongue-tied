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

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * This predicate is used to find {@link Translation}s based off its 
 * business keys of {@link Bundle}, {@link Country} and {@link Language}.
 * 
 * @author bsion
 *
 */
public final class TranslationPredicate implements Predicate
{
    private Bundle bundle;
    private Country country;
    private Language language;
    
    /**
     * Create a new instance of TranslationPredicate.
     * 
     * @param bundle the {@link Bundle} on which to search
     * @param country the {@link Country} on which to search
     * @param language the {@link Language} on which to search
     */
    public TranslationPredicate(final Bundle bundle, final Country country, final Language language)
    {
        this.bundle = bundle;
        this.country = country;
        this.language = language;
    }
    
    /** 
     * Evaluate if a {@link Translation}s business keys are equal. This  
     * method evaluates if the {@link Language}, {@link Bundle} and
     * {@link Country} are equal
     * 
     * @return <code>true</code> if the {@link Translation} business keys
     * match. <code>false</code> otherwise
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public boolean evaluate(Object object)
    {
        Translation translation = (Translation) object;
        
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(language, translation.getLanguage()).
            append(country, translation.getCountry()).
            append(bundle, translation.getBundle());
        
        return builder.isEquals();
    }

    /**
     * @return the bundle
     */
    public final Bundle getBundle()
    {
        return bundle;
    }

    /**
     * @return the country
     */
    public final Country getCountry()
    {
        return country;
    }

    /**
     * @return the language
     */
    public final Language getLanguage()
    {
        return language;
    }
}