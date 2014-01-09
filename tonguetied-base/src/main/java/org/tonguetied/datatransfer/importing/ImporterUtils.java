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
package org.tonguetied.datatransfer.importing;

import java.util.List;

import org.apache.log4j.Logger;
import org.tonguetied.datatransfer.importing.ImportException.ImportErrorCode;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * Utility class containing methods used during importing data.
 * 
 * @author bsion
 *
 */
public class ImporterUtils
{
    private static final Logger logger = Logger.getLogger(ImporterUtils.class);

    /**
     * Find the {@link LanguageCode} based on the string <code>code</code>. If
     * the code is not a valid enum value then an 
     * {@link ImportErrorCode#illegalLanguage} is added.
     * 
     * @param code the string code to evaluate
     * @param errorCodes the list of existing {@link ImportErrorCode}
     * @return the {@link LanguageCode} matching <code>code</code> or 
     * <code>null</code> if no match is found
     */
    public static LanguageCode evaluateLanguageCode(final String code, List<ImportErrorCode> errorCodes)
    {
        LanguageCode languageCode = null;
        try
        {
            languageCode = LanguageCode.valueOf(code);
            if (logger.isDebugEnabled())
                logger.debug("languageCode = " + languageCode);
        }
        catch (IllegalArgumentException iae)
        {
            errorCodes.add(ImportErrorCode.illegalLanguage);
        }
        catch (NullPointerException npe)
        {
            errorCodes.add(ImportErrorCode.illegalLanguage);
        }
        return languageCode;
    }

    /**
     * Find the {@link CountryCode} based on the string <code>code</code>. If
     * the code is not a valid enum value then an 
     * {@link ImportErrorCode#illegalCountry} is added.
     * 
     * @param code the string code to evaluate
     * @param errorCodes the list of existing {@link ImportErrorCode}
     * @return the {@link CountryCode} matching <code>code</code> or 
     * <code>null</code> if no match is found
     */
    public static CountryCode evaluateCountryCode(final String code, List<ImportErrorCode> errorCodes)
    {
        CountryCode countryCode = null;
        try
        {
            countryCode = CountryCode.valueOf(code);
            if (logger.isDebugEnabled())
                logger.debug("countryCode = " + countryCode);
        }
        catch (IllegalArgumentException iae)
        {
            errorCodes.add(ImportErrorCode.illegalCountry);
        }
        catch (NullPointerException npe)
        {
            errorCodes.add(ImportErrorCode.illegalCountry);
        }
        return countryCode;
    }
    
    /**
     * Find the {@link TranslationState} based on the string <code>code</code>.
     * If the code is not a valid enum value then an 
     * {@link ImportErrorCode#illegalTranslationState} is added.
     * 
     * @param stateStr the string to evaluate
     * @param errorCodes the list of existing {@link ImportErrorCode}
     * @return the {@link TranslationState} matching <code>state</code> or 
     * <code>null</code> if no match is found
     */
    public static TranslationState evaluateTranslationState(final String stateStr, List<ImportErrorCode> errorCodes)
    {
        TranslationState state = null;
        try
        {
            state = TranslationState.valueOf(stateStr);
            if (logger.isDebugEnabled())
                logger.debug("translationState = " + state);
        }
        catch (IllegalArgumentException iae)
        {
            errorCodes.add(ImportErrorCode.illegalTranslationState);
        }
        catch (NullPointerException npe)
        {
            errorCodes.add(ImportErrorCode.illegalTranslationState);
        }
        
        return state;
    }
}
