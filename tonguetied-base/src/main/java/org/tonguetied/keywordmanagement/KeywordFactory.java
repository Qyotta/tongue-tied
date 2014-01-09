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

import org.apache.log4j.Logger;
import org.tonguetied.keywordmanagement.Translation.TranslationState;


/**
 * Implementation of Factory pattern to externalize the creation of keywords.
 * 
 * @author bsion
 *
 */
public class KeywordFactory
{
    private static final Logger logger = Logger.getLogger(KeywordFactory.class);

    /**
     * Create a new {@link Keyword} with a new {@link Translation} for each 
     * {@link Language} provided. This factory method instantiates each
     * {@link Translation} with a {@link Language} from <code>languages</code>
     * and the {@link Country} from <code>country</code>.
     * 
     * @param languages the list of {@link Language}s for which translations 
     * should be created
     * @param country the default {@link Country} for which each translation 
     * should be created
     * @param bundle the default {@link Bundle} for which each translation 
     * should be created
     * @return A new {@link Keyword} with a list of predefined 
     * {@link Translation}s
     * @throws IllegalArgumentException if the list of {@link Language}s is 
     * <code>null</code>
     */
    public static Keyword createKeyword(List<Language> languages, Country country, Bundle bundle) 
            throws IllegalArgumentException
    {
        if (languages == null)
        {
            throw new IllegalArgumentException(
                    "Cannot provide a null list of languages");
        }
        
        if (logger.isDebugEnabled()) 
            logger.debug("creating new keyword based on list of languages");
        
        Keyword keyword = new Keyword();
        for(Language language: languages)
        {
            Translation translation = 
                new Translation(bundle, country, language, null, TranslationState.UNVERIFIED);
            keyword.addTranslation(translation);
        }
        
        return keyword;
    }
    
    /**
     * Create a new {@link Keyword} with a new {@link Translation} for each 
     * {@link Country} provided. This factory method instantiates each
     * {@link Translation} with a {@link Country} from <code>countries</code>
     * and the {@link Language} from <code>language</code>.
     * 
     * @param countries the list of {@link Country}s for which translations 
     * should be created
     * @param language the default {@link Language} for which each translation 
     * should be created
     * @param bundle the default {@link Bundle} for which each translation 
     * should be created
     * @return A new {@link Keyword} with a list of predefined 
     * {@link Translation}s
     * @throws IllegalArgumentException if the list of {@link Country}s is 
     * <code>null</code>
     */
    public static Keyword createKeyword(List<Country> countries, Language language, Bundle bundle) 
            throws IllegalArgumentException
    {
        if (countries == null)
        {
            throw new IllegalArgumentException(
                    "Cannot provide a null list of countries");
        }
        
        if (logger.isDebugEnabled()) 
            logger.debug("creating new keyword based on list of countries");
        
        Keyword keyword = new Keyword();
        for(Country country: countries)
        {
            Translation translation = 
                new Translation(bundle, country, language, null, TranslationState.UNVERIFIED);
            keyword.addTranslation(translation);
        }
        
        return keyword;
    }
}
