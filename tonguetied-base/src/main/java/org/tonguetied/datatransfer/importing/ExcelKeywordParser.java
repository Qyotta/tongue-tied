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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.tonguetied.datatransfer.common.FormatType;
import org.tonguetied.datatransfer.importing.ImportException.ImportErrorCode;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;

/**
 * This class parses excel spreadsheets in the format for {@link FormatType#XLS}
 * . A map of {@link Keyword}s and their {@link Translation}s are built by
 * processing each cell of the spread sheet.
 * 
 * @author bsion
 * 
 */
public class ExcelKeywordParser implements ExcelParser {
    private static final Logger logger = Logger.getLogger(ExcelKeywordParser.class);

    private SSTRecord sstrec;
    private Map<String, Keyword> keywords;
    private Keyword keyword;
    private Translation baseTranslation;
    private RowType rowType;
    private KeywordService keywordService;
    private List<ImportErrorCode> errorCodes;

    public void setKeywordService(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    /**
     * Create a new instance of ExcelKeywordParser.
     * 
     * @param keywordService
     */
    public ExcelKeywordParser() {
        this.keywords = new HashMap<String, Keyword>();
        this.errorCodes = new ArrayList<ImportErrorCode>();
    }

    public Map<String, Keyword> getKeywords() {
        return keywords;
    }

    public List<ImportErrorCode> getErrorCodes() {
        return errorCodes;
    }

    public void processRecord(Record record) {
        if (record == null) {
            if (logger.isInfoEnabled())
                logger.info("no record to process");
        } else {
            switch (record.getSid()) {
                // the BOFRecord can represent either the beginning of a sheet
                // or the workbook
                case BOFRecord.sid:
                    if (!(record instanceof BOFRecord))
                        throw new ImportException("unknown excel element", null);
                    final BOFRecord bof = (BOFRecord) record;
                    if (bof.getType() == BOFRecord.TYPE_WORKBOOK) {
                        if (logger.isInfoEnabled())
                            logger.info("Processing excel workbook");
                        // assigned to the class level member
                    } else if (bof.getType() == BOFRecord.TYPE_WORKSHEET) {
                        if (logger.isInfoEnabled())
                            logger.info("recordsize = " + bof.getRecordSize() + ", required version = " + bof.getRequiredVersion());
                    }
                    break;
                case BoundSheetRecord.sid:
                    if (!(record instanceof BoundSheetRecord))
                        throw new ImportException("unknown excel element", null);
                    final BoundSheetRecord bsr = (BoundSheetRecord) record;
                    // sheets named have no impact on generating query
                    if (logger.isDebugEnabled())
                        logger.debug("processing sheet: " + bsr.getSheetname());
                    break;
                case RowRecord.sid:
                    if (!(record instanceof RowRecord))
                        throw new ImportException("unknown excel element", null);
                    if (logger.isDebugEnabled()) {
                        final RowRecord rowrec = (RowRecord) record;
                        logger.debug("processing row: " + rowrec.getRowNumber());
                    }
                    break;
                case NumberRecord.sid:
                    if (!(record instanceof NumberRecord))
                        throw new ImportException("unknown excel element", null);
                    final NumberRecord numrec = (NumberRecord) record;
                    logger.warn("Cell [" + numrec.getRow() + "," + numrec.getColumn() + "] expecting a string value not numeric: "
                            + numrec.getValue() + ". Ignoring value");
                    break;
                case SSTRecord.sid:
                    if (!(record instanceof SSTRecord))
                        throw new ImportException("unknown excel element", null);
                    // SSTRecords store a array of unique strings used in Excel.
                    sstrec = (SSTRecord) record;
                    if (logger.isDebugEnabled()) {
                        logger.debug("file contains " + sstrec.getNumUniqueStrings() + " unique strings");
                    }
                    break;
                case LabelSSTRecord.sid:
                    if (!(record instanceof LabelSSTRecord))
                        throw new ImportException("unknown excel element", null);
                    final LabelSSTRecord lrec = (LabelSSTRecord) record;
                    if (lrec.getRow() != 0) {
                        if (lrec.getColumn() == 0) {
                            evaluateRowType(lrec);
                        } else {
                            final String cellValue = sstrec.getString(lrec.getSSTIndex()).getString();
                            if (lrec.getColumn() == 1) {
                                switch (rowType) {
                                    case keyword:
                                        // there were no translations for the
                                        // previous keyword, so add to keywords
                                        if (keyword != null && keyword.getTranslations().isEmpty())
                                            keywords.put(keyword.getKeyword(), keyword);
                                        loadKeyword(cellValue);
                                        break;
                                    case context:
                                        if (StringUtils.isNotBlank(cellValue))
                                            keyword.setContext(cellValue);
                                        break;
                                    default:
                                        break;
                                }
                            } else if (lrec.getColumn() == 2) {
                                baseTranslation = new Translation();
                                baseTranslation.setKeyword(keyword);
                                final LanguageCode code = ImporterUtils.evaluateLanguageCode(cellValue, errorCodes);
                                Language language = null;
                                if (code != null) {
                                    language = keywordService.getLanguage(code);
                                    if (language == null)
                                        errorCodes.add(ImportErrorCode.unknownLanguage);
                                }
                                baseTranslation.setLanguage(language);
                            } else if (lrec.getColumn() == 4) {
                                final CountryCode code = ImporterUtils.evaluateCountryCode(cellValue, errorCodes);
                                Country country = null;
                                if (code != null) {
                                    country = keywordService.getCountry(code);
                                    if (country == null)
                                        errorCodes.add(ImportErrorCode.unknownCountry);
                                }
                                baseTranslation.setCountry(country);
                            } else if (lrec.getColumn() == 6) {
                                final Bundle bundle = keywordService.getBundleByName(cellValue);
                                if (bundle == null)
                                    errorCodes.add(ImportErrorCode.unknownBundle);
                                baseTranslation.setBundle(bundle);
                            } else if (lrec.getColumn() == 7) {
                                final TranslationState state = ImporterUtils.evaluateTranslationState(cellValue, errorCodes);
                                baseTranslation.setState(state);
                            } else if (lrec.getColumn() == 8) {
                                baseTranslation.setValue(cellValue);
                                keyword.addTranslation(baseTranslation);
                                keywords.put(keyword.getKeyword(), keyword);
                            }
                        }

                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Determine the type of the keyword attribute for the excel row.
     * 
     * @param lrec
     *            the excel cell to evaluate
     */
    private void evaluateRowType(final LabelSSTRecord lrec) {
        final String value = sstrec.getString(lrec.getSSTIndex()).getString();
        if ("Keyword".equals(value))
            rowType = RowType.keyword;
        else if ("Context".equals(value))
            rowType = RowType.context;
    }

    /**
     * @param keywordStr
     *            the keyword string to evaluate
     */
    private void loadKeyword(final String keywordStr) {
        keyword = keywords.get(keywordStr);
        if (keyword == null) {
            if (logger.isDebugEnabled())
                logger.debug("creating new keyword instance");
            keyword = new Keyword();
            keyword.setKeyword(keywordStr);
        }
    }

    private static enum RowType {
        keyword, context
    }
}
