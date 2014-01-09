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

/**
 * Listing of all constants for web tier.
 * 
 * @author bsion
 *
 */
public interface Constants
{
    static final String APPLY_FILTER = "applyFilter";
    static final String AUDIT_LOG = "auditLog";
    static final String BTN_SEARCH = "searchBtn";
    static final String BUNDLE_ID = "bundleId";
    static final String BUNDLES = "bundles";
    static final String COUNTRIES = "countries";
    static final String COUNTRY = "country";
    static final String COUNTRY_ID = "countryId";
    static final String FORMAT_TYPES = "formatTypes";
    static final String LANGUAGE = "language";
    static final String LANGUAGE_ID = "languageId";
    static final String LANGUAGES = "languages";
    static final String MAX_LIST_SIZE = "maxListSize";
    static final String KEYWORD_ID = "keywordId";
    static final String KEYWORDS = "keywords";
    static final String PAGE_SIZES = "pageSizes";
    static final String SEARCH_PARAMETERS = "searchParameters";
    static final String SHOW_ALL_KEYWORDS = "showAllKeywords";
    static final String SHOW_ALL_USERS = "showAllUsers";
    static final String STATES = "states";
    static final String USER = "user";
    static final String USER_SIZE = "userSize";
    static final String USERS = "users";
    static final String TABLE_ID_KEYWORD = "keyword";
    static final String TABLE_ID_USER = "user";
    static final String VIEW_PREFERENCES = "viewPreferences";

    static final char SPACE_SEPARATOR = ' ';
    static final char LINE_SEPARATOR = '\u2007';
    static final char PARAGRAPH_SEPARATOR = '\u202F';
    static final char HORIZONTAL_TABULATION = '\u0009';
    static final char VERTICAL_TABULATION = '\u000B';
    static final char FORM_FEED = '\u000C';
    static final char FILE_SEPARATOR = '\u001C';
    static final char GROUP_SEPARATOR = '\u001D';
    static final char RECORD_SEPARATOR = '\u001E';
    static final char UNIT_SEPARATOR = '\u001F';
    /*
    static final char LINE_FEED = '\u000A';
//    private static final char CARRIAGE_RETURN = '\u000D';
 */

    static final int DEFAULT_AUDIT_LOG_PAGE_SIZE = 50;
    static final int DEFAULT_USER_PAGE_SIZE = 10;
}
