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
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.tonguetied.datatransfer.dao.TransferRepository;
import org.tonguetied.datatransfer.importing.ImportException.ImportErrorCode;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.TranslationPredicate;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * Data importer that handles input in excel format. The excel file is read and
 * rows are transformed into {@link Translation}s
 * 
 * @author bsion
 * 
 */
public class ExcelImporter extends AbstractImporter {
    private ExcelParser parser;
    private TransferRepository transferRepository;

    public void setParser(ExcelParser parser) {
        this.parser = parser;
    }

    public void setTransferRepository(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public void doImport(byte[] input, TranslationState state) throws ImportException {
        loadData(input);
        List<ImportErrorCode> errorCodes = parser.getErrorCodes();
        if (!errorCodes.isEmpty())
            throw new ImportException(errorCodes);

        doImport(parser.getKeywords());
    }

    /**
     * This method initializes the parser enabling the parser to handle the
     * excel document.
     * 
     * @param input
     *            the byte code representation of the excel document
     * @throws ImportException
     *             if the input data fails to be parsed
     */
    private void loadData(byte[] input) throws ImportException {
        ByteArrayInputStream bais = null;
        InputStream dis = null;
        try {
            bais = new ByteArrayInputStream(input);
            // create a new org.apache.poi.poifs.filesystem.Filesystem
            POIFSFileSystem poifs = new POIFSFileSystem(bais);
            // get the Workbook (excel part) stream in a InputStream
            dis = poifs.createDocumentInputStream("Workbook");
            // construct out HSSFRequest object
            HSSFRequest req = new HSSFRequest();
            // lazy listen for ALL records with the listener shown above
            req.addListenerForAllRecords(parser);
            // create our event factory
            HSSFEventFactory factory = new HSSFEventFactory();
            // process our events based on the document input stream
            factory.processEvents(req, dis);
        } catch (IOException ioe) {
            throw new ImportException(ioe);
        } finally {
            // and our document input stream (don't want to leak these!)
            close(dis);
            // once all the events are processed close our file input stream
            close(bais);
        }
    }

    /**
     * Perform the actual import of data into persistent storage.
     * 
     * @param keywords
     *            the map of {@link Keyword}s to import.
     */
    private void doImport(Map<String, Keyword> keywords) {
        Keyword keyword;
        for (Map.Entry<String, Keyword> entry : keywords.entrySet()) {
            keyword = getKeywordService().getKeyword(entry.getKey());
            if (keyword == null) {
                keyword = entry.getValue();
            } else {
                // find translation by business key
                Predicate predicate;
                Translation translation;
                for (Translation refTranslation : entry.getValue().getTranslations()) {
                    predicate = new TranslationPredicate(refTranslation.getBundle(), refTranslation.getCountry(), refTranslation
                            .getLanguage());
                    translation = (Translation) CollectionUtils.find(keyword.getTranslations(), predicate);
                    if (translation == null) {
                        keyword.addTranslation(refTranslation);
                    } else {
                        translation.setValue(refTranslation.getValue());
                    }
                }
            }

            transferRepository.saveOrUpdate(keyword);
        }

        if (logger.isInfoEnabled())
            logger.info("processed " + keywords.size() + " translations");
    }

    /**
     * Helper method to safely close an {@link InputStream}.
     * 
     * @param is
     *            the {@link InputStream} to close
     * @throws ImportException
     *             if the stream fails to close
     */
    private void close(InputStream is) {
        try {
            if (is != null)
                is.close();
        } catch (IOException ioe) {
            throw new ImportException(ioe);
        }
    }
}
