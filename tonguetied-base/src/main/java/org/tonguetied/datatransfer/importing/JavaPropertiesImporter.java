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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.IOUtils;
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

/**
 * Data importer that handles input in the Java resource or property file
 * format. The resource file is read and entries are transformed into
 * {@link Translation}s and added to the system.
 * 
 * @author bsion
 * @see Properties
 */
public class JavaPropertiesImporter extends AbstractSingleResourceImporter {

    @Override
    protected void doImport(final byte[] input, final TranslationState state) throws ImportException {
        ByteArrayInputStream bais = null;
        try {
            // convert byte array into UTF-8 format rather than rely on the
            // default string encoding for this JVM
            final String inputString = new String(input, "UTF-8");
            bais = new ByteArrayInputStream(inputString.getBytes());
            Properties properties = new Properties();
            properties.load(bais);
            Keyword keyword;
            Translation translation;
            String value;
            for (Entry<Object, Object> entry : properties.entrySet()) {
                keyword = getKeywordService().getKeyword((String) entry.getKey());
                value = "".equals(entry.getValue()) ? null : (String) entry.getValue();
                if (keyword == null) {
                    keyword = new Keyword();
                    keyword.setKeyword((String) entry.getKey());
                    translation = new Translation(getBundle(), getCountry(), getLanguage(), value, state);
                    keyword.addTranslation(translation);
                } else {
                    translation = findTranslation(keyword);
                    if (translation == null) {
                        translation = new Translation(getBundle(), getCountry(), getLanguage(), value, state);
                        keyword.addTranslation(translation);
                    } else {
                        translation.setState(state);
                        translation.setValue(value);
                    }
                }

                getKeywordService().saveOrUpdate(keyword);
            }
            if (logger.isInfoEnabled())
                logger.info("processed " + properties.size() + " translations");
        } catch (IOException ioe) {
            throw new ImportException(ioe);
        } finally {
            IOUtils.closeQuietly(bais);
        }
    }

    /**
     * Find a translation from an existing keyword that matches the business
     * keys.
     * 
     * @param keyword
     *            the existing keyword to search
     * @return the matching translation or <code>null</code> if no match is
     *         found
     */
    private Translation findTranslation(final Keyword keyword) {
        Translation translation = null;
        if (!keyword.getTranslations().isEmpty()) {
            final Predicate predicate = new TranslationPredicate(getBundle(), getCountry(), getLanguage());
            translation = (Translation) CollectionUtils.find(keyword.getTranslations(), predicate);
        }

        return translation;
    }

    /**
     * Validates the <code>fileName</code> to ensure that the fileName
     * corresponds to an existing {@link Bundle}, {@link Country} and
     * {@link Language}.
     * 
     */
    @Override
    protected void validate(final String fileName, final Bundle bundle, List<ImportErrorCode> errorCodes) throws ImportException {
        String[] tokens = fileName.split("_");

        CountryCode countryCode = null;
        LanguageCode languageCode = null;
        switch (tokens.length) {
            case 1:
                // this is the default bundle, so no country or language
                countryCode = CountryCode.DEFAULT;
                languageCode = LanguageCode.DEFAULT;
                break;
            case 2:
                if (isCountryCode(tokens[1])) {
                    countryCode = ImporterUtils.evaluateCountryCode(tokens[1], errorCodes);
                    languageCode = LanguageCode.DEFAULT;
                } else {
                    countryCode = CountryCode.DEFAULT;
                    languageCode = ImporterUtils.evaluateLanguageCode(tokens[1], errorCodes);
                }
                break;
            case 3:
                countryCode = ImporterUtils.evaluateCountryCode(tokens[2], errorCodes);
                languageCode = ImporterUtils.evaluateLanguageCode(tokens[1], errorCodes);
                break;
            default:
                errorCodes.add(ImportErrorCode.invalidNameFormat);
                break;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("bundle name = " + tokens[0]);
        }

        if (bundle == null) {
            setBundle(getKeywordService().getBundleByResourceName(tokens[0]));
            if (getBundle() == null)
                errorCodes.add(ImportErrorCode.unknownBundle);
        } else {
            setBundle(bundle);
        }

        this.setCountry(getKeywordService().getCountry(countryCode));
        if (getCountry() == null)
            errorCodes.add(ImportErrorCode.unknownCountry);

        this.setLanguage(getKeywordService().getLanguage(languageCode));
        if (getLanguage() == null)
            errorCodes.add(ImportErrorCode.unknownLanguage);

        if (!errorCodes.isEmpty())
            logger.warn("Cannot process " + fileName + ". It contains " + errorCodes.size() + " errors");
    }

    /**
     * Determines if the string component is a country code or not.
     * 
     * @param code
     *            the code to evaluate
     * @return <code>true</code> if the string corresponds to a potential
     *         country code, <code>false</code> otherwise
     */
    protected boolean isCountryCode(String code) {
        boolean isCountryCode = false;
        if (code != null && !"".equals(code))
            isCountryCode = Character.isUpperCase(code.charAt(0));

        return isCountryCode;
    }
}
