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

import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Language;

/**
 * Abstract class for importers that process a input data that relates to a
 * specific resource combination of {@link Bundle}, {@link Country} and
 * {@link Language}.
 * 
 * @author bsion
 * 
 */
public abstract class AbstractSingleResourceImporter extends AbstractImporter {
    private Bundle bundle;
    private Country country;
    private Language language;

    /**
     * @return the {@link Bundle} the imported file corresponds to
     */
    protected Bundle getBundle() {
        return bundle;
    }

    /**
     * Assign the bundle.
     * 
     * @param bundle
     *            the bundle to set
     */
    protected void setBundle(final Bundle bundle) {
        this.bundle = bundle;
    }

    /**
     * @return the {@link Country} the imported file corresponds to
     */
    protected Country getCountry() {
        return country;
    }

    /**
     * Assign the country.
     * 
     * @param country
     *            the country to set
     */
    protected void setCountry(final Country country) {
        this.country = country;
    }

    /**
     * @return the {@link Language} the imported file corresponds to
     */
    protected Language getLanguage() {
        return language;
    }

    /**
     * Assign the language.
     * 
     * @param language
     *            the language to set
     */
    protected void setLanguage(final Language language) {
        this.language = language;
    }
}
