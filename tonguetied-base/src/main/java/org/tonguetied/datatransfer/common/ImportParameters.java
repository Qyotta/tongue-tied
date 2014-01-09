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
package org.tonguetied.datatransfer.common;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * Value object to transport export selection criteria. This object does not
 * have a state.
 * 
 * @author bsion
 * 
 */
public class ImportParameters
{

    private String fileName;
    private byte[] data;
    private FormatType formatType;
    private TranslationState translationState;
    private Bundle bundle;

    /**
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(final String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * The raw data to import. This data can be in text or binary format. This
     * will be specific to the importer, as data can be imported from different
     * sources.
     * 
     * @return the raw data to import
     */
    public byte[] getData()
    {
        return data;
    }

    /**
     * @param data the data to be translated to Keywords and Translations
     */
    public void setData(final byte[] data)
    {
        // assign a copy of this parameter to avoid later changes to mutable 
        // object
//        this.data = (byte[])data.clone();
        this.data = data;
    }

    /**
     * @return the format of the uploaded file
     */
    public FormatType getFormatType()
    {
        return formatType;
    }

    /**
     * @param formatType the format of the uploaded file
     */
    public void setFormatType(final FormatType formatType)
    {
        this.formatType = formatType;
    }

    /**
     * @return the {@link TranslationState} to set all imported
     *         {@link Translation}s
     */
    public TranslationState getTranslationState()
    {
        return translationState;
    }

    /**
     * @param translationState the {@link TranslationState} to set all imported
     *        {@link Translation}s
     */
    public void setTranslationState(final TranslationState translationState)
    {
        this.translationState = translationState;
    }

    /**
     * @return the {@link Bundle} used to map this file to for Java property 
     * and .Net resource bundles.
     */
    public Bundle getBundle()
    {
        return bundle;
    }

    /**
     * @param bundle the {@link Bundle} to set for resource matching to a bundle
     */
    public void setBundle(final Bundle bundle)
    {
        this.bundle = bundle;
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this,
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
