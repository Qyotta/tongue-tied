/*
 * Copyright 2008 The Tongue-Tied Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.tonguetied.datatransfer.importing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.tonguetied.datatransfer.importing.ImportException.ImportErrorCode;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.TranslationPredicate;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Data importer that handles input in the csv file format. The csv file is read
 * and entries are transformed into {@link Translation}s and added to the
 * system.
 * 
 * Used rules for csv format from <a
 * href="http://www.creativyst.com/Doc/Articles/CSV/CSV01.htm">Csv Format
 * Rules</a>
 * 
 * @author bsion
 * 
 */
public class CsvImporter extends AbstractImporter {
    private List<ImportErrorCode> errorCodes;
    private int keywordCount;

    private static final int INDEX_BUNDLE_NAME = 6;
    private static final int INDEX_CONTEXT = 1;
    private static final int INDEX_COUNTRY_CODE = 4;
    private static final int INDEX_KEYWORD = 0;
    private static final int INDEX_LANGUAGE_CODE = 2;
    private static final int INDEX_TRANSLATION_STATE = 7;
    private static final int INDEX_VALUE = 8;

    /**
     * Create a new instance of CsvImporter.
     * 
     * @param keywordService
     *            the interface to keyword functions
     */
    public CsvImporter() {
        this.errorCodes = new ArrayList<ImportErrorCode>();
        this.keywordCount = 0;
    }

    @Override
    protected void doImport(final byte[] input, final TranslationState state) throws ImportException {
        BufferedReader bufReader = null;
        try {
            String data = new String(input);
            bufReader = new BufferedReader(new StringReader(data));
            CSVReader reader = new CSVReader(bufReader, CSVReader.DEFAULT_SEPARATOR, CSVReader.DEFAULT_QUOTE_CHARACTER);
            String[] nextLine = reader.readNext();
            String keywordStr = null;
            Keyword keyword = null;
            Language language;
            Country country;
            Bundle bundle;
            TranslationState translationState;
            while (nextLine != null) {
                if (!StringUtils.equals(keywordStr, nextLine[INDEX_KEYWORD])) {
                    keywordStr = nextLine[INDEX_KEYWORD];
                    keyword = getKeyword(keywordStr);
                }

                if (!StringUtils.equals(keyword.getContext(), nextLine[INDEX_CONTEXT]))
                    keyword.setContext(nextLine[INDEX_CONTEXT]);

                language = evaluateLanguage(nextLine[INDEX_LANGUAGE_CODE]);
                country = evaluateCountry(nextLine[INDEX_COUNTRY_CODE]);
                bundle = evaluateBundle(nextLine[INDEX_BUNDLE_NAME]);
                translationState = ImporterUtils.evaluateTranslationState(nextLine[INDEX_TRANSLATION_STATE], errorCodes);

                if (!errorCodes.isEmpty())
                    throw new ImportException(errorCodes);

                updateTranslation(keyword, language, country, bundle, translationState, nextLine[INDEX_VALUE]);
                getKeywordService().saveOrUpdate(keyword);
                keywordCount++;
                nextLine = reader.readNext();
            }
        } catch (IOException ioe) {
            throw new ImportException(ioe);
        } finally {
            // once all the events are processed close our file input stream
            IOUtils.closeQuietly(bufReader);
        }

        if (logger.isInfoEnabled())
            logger.info("processed " + keywordCount + " translations");
    }

    /**
     * Add or update a translation for a keyword.
     * 
     * @param keyword
     *            the current {@link Keyword}
     * @param language
     *            the current {@link Language}
     * @param country
     *            the current {@link Country}
     * @param bundle
     *            the current {@link Bundle}
     * @param translationState
     *            the {@link TranslationState} for the translation
     * @param value
     *            the value of the translation
     */
    private void updateTranslation(Keyword keyword, final Language language, final Country country, final Bundle bundle,
            final TranslationState translationState, final String value) {
        Predicate predicate = new TranslationPredicate(bundle, country, language);
        Translation translation = (Translation) CollectionUtils.find(keyword.getTranslations(), predicate);

        if (translation == null) {
            keyword.addTranslation(new Translation(bundle, country, language, evaluateValue(value), translationState));
        } else {
            translation.setState(translationState);
            translation.setValue(evaluateValue(value));
        }
    }

    /**
     * Find the {@link Bundle} based on the string <code>code</code>. If the
     * code is not a valid enum value then an
     * {@link ImportErrorCode#unknownBundle} is added.
     * 
     * @param name
     *            the string to evaluate
     * @return the {@link Bundle} matching <code>name</code> or
     *         <code>null</code> if no match is found
     */
    private Bundle evaluateBundle(final String name) {
        Bundle bundle = getKeywordService().getBundleByName(name);
        if (bundle == null)
            errorCodes.add(ImportErrorCode.unknownBundle);

        if (logger.isDebugEnabled())
            logger.debug("bundle = " + bundle);

        return bundle;
    }

    /**
     * Process the value to be stored
     * 
     * @param value
     *            the value to be processed
     * @return <code>null</code> if the value is the empty string (""),
     *         otherwise the unchanged string
     */
    private String evaluateValue(final String value) {
        return "".equals(value) ? null : value;
    }

    /**
     * Find the {@link CountryCode} based on the string <code>code</code>. If
     * the code is not a valid enum value then an
     * {@link ImportErrorCode#illegalCountry} is added. If the code is valid but
     * does not exist in the system then an
     * {@link ImportErrorCode#unknownCountry} is added.
     * 
     * @param code
     *            the string value of the {@link CountryCode}
     * @return the matching {@link Country} in the system of the
     *         <code>code</code>, or <code>null</code> if no match is found
     */
    private Country evaluateCountry(final String code) {
        Country country = null;
        CountryCode countryCode = ImporterUtils.evaluateCountryCode(code, errorCodes);
        if (countryCode != null) {
            country = getKeywordService().getCountry(countryCode);
            if (country == null)
                errorCodes.add(ImportErrorCode.unknownCountry);
        }
        return country;
    }

    /**
     * Find the {@link LanguageCode} based on the string <code>code</code>. If
     * the code is not a valid enum value then an
     * {@link ImportErrorCode#illegalLanguage} is added. If the code is valid
     * but does not exist in the system then an
     * {@link ImportErrorCode#unknownLanguage} is added.
     * 
     * @param code
     *            the string value of the {@link LanguageCode}
     * @return the matching {@link Language} in the system of the
     *         <code>code</code>, or <code>null</code> if no match is found
     */
    private Language evaluateLanguage(final String code) {
        Language language = null;
        LanguageCode languageCode = ImporterUtils.evaluateLanguageCode(code, errorCodes);
        if (languageCode != null) {
            language = getKeywordService().getLanguage(languageCode);
            if (language == null)
                errorCodes.add(ImportErrorCode.unknownLanguage);
        }

        return language;
    }

    /**
     * Find an existing {@link Keyword} matching the input
     * <code>keywordStr</code>. If one does not then create a new one.
     * 
     * @param keywordStr
     *            the key of the keyword
     * @return the keyword for the input string
     */
    private Keyword getKeyword(final String keywordStr) {
        Keyword keyword = getKeywordService().getKeyword(keywordStr);
        if (keyword == null) {
            keyword = new Keyword();
            keyword.setKeyword(keywordStr);
        }

        return keyword;
    }

}
