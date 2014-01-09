package org.tonguetied.datatransfer.importing;

import org.tonguetied.datatransfer.common.ImportParameters;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

public interface Importer {

    /**
     * Import {@link Keyword}/{@link Translation}s into the system. If the
     * {@link ImportParameters#getTranslationState()} is <code>null</code> then
     * the <code>TranslationState<code> is set to
     * {@link TranslationState#UNVERIFIED} for all imported 
     * {@link Translation}s
     * 
     * @param parameters
     *            the parameters used to process import data
     * @throws ImportException
     *             if an error occurs during the whole import process. The
     *             {@link ImportException} may contain additional information as
     *             to why the import failed
     */
    void importData(ImportParameters parameters) throws ImportException;

}