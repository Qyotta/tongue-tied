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
import org.tonguetied.keywordmanagement.Bundle;

/**
 * Support class to map enum {@link Bundle} to a string key in the web tier.
 * This class is used for rendering purposes.
 * 
 * @author bsion
 * 
 */
public class BundleSupport extends PropertyEditorSupport
{
    private Map<Long, Bundle> bundles;

    /**
     * Create new instance of CountrySupport.
     * 
     * @param b the list of current {@link Bundle}s
     */
    public BundleSupport(List<Bundle> b)
    {
        this.bundles = new HashMap<Long, Bundle>();
        for (Bundle bundle : b)
        {
            bundles.put(bundle.getId(), bundle);
        }
    }

    @Override
    public String getAsText()
    {
        final Object value = getValue();
        return value == null ? "" : ((Bundle) value).getId().toString();
    }

    @Override
    public void setAsText(String string) throws IllegalArgumentException
    {
        Bundle value = null;

        if (StringUtils.isNotEmpty(string))
        {
            final Long id = Long.valueOf(string);
            if (bundles.containsKey(id))
            {
                value = bundles.get(id);
            }
            else
            {
                throw new IllegalArgumentException("Bundle with id " + string
                        + " not found");
            }
        }

        setValue(value);
    }
}
