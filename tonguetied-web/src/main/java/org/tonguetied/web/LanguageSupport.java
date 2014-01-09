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

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tonguetied.keywordmanagement.Language;

/**
 * Support class to map enum {@link Language} to a string key in the web tier.
 * This class is used for rendering purposes.
 * 
 * @author bsion
 * 
 */
public class LanguageSupport extends PropertyEditorSupport
{
    private Map<Long, Language> languages;

    /**
     * Create new instance of LanguageSupport.
     * 
     * @param l the list of current {@link Language}s
     */
    public LanguageSupport(List<Language> l)
    {
        languages = new HashMap<Long, Language>();
        for (Language language : l)
        {
            languages.put(language.getId(), language);
        }
    }

    @Override
    public String getAsText()
    {
        final Object value = getValue();
        return value == null ? "" : ((Language) value).getId().toString();
    }

    @Override
    public void setAsText(final String string) throws IllegalArgumentException
    {
        Language value = null;

        if (StringUtils.isNotEmpty(string))
        {
            final Long id = Long.valueOf(string);
            if (languages.containsKey(id))
            {
                value = languages.get(id);
            }
            else
            {
                throw new IllegalArgumentException("Language with id " + string
                        + " not found");
            }
        }

        setValue(value);
    }
}
