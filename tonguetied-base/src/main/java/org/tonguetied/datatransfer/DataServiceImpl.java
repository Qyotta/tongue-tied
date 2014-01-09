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
package org.tonguetied.datatransfer;

import static freemarker.log.Logger.LIBRARY_LOG4J;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.datatransfer.common.ImportParameters;
import org.tonguetied.datatransfer.dao.TransferRepository;
import org.tonguetied.datatransfer.exporting.ExportException;
import org.tonguetied.datatransfer.exporting.Exporter;
import org.tonguetied.datatransfer.exporting.ExporterFactory;
import org.tonguetied.datatransfer.importing.Importer;
import org.tonguetied.datatransfer.importing.ImporterFactory;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.TranslationKeywordPredicate;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;

/**
 * Concrete implementation of the {@link DataService} interface.
 * 
 * @author bsion
 * 
 */
public class DataServiceImpl implements DataService {
    private static final Logger logger = Logger.getLogger(DataServiceImpl.class);

    private TransferRepository transferRepository;
    private KeywordService keywordService;

    private ExporterFactory exporterFactory;
    private ImporterFactory importerFactory;

    public void setTransferRepository(final TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    public void setKeywordService(final KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    public void setExporterFactory(ExporterFactory exporterFactory) {
        this.exporterFactory = exporterFactory;
    }

    public void setImporterFactory(ImporterFactory importerFactory) {
        this.importerFactory = importerFactory;
    }

    /**
     * Initialize an instance of the DataServiceImpl. This method configures the
     * exporter for use.
     * 
     * @throws ExportException
     *             if the exporter is fails to configure
     */
    public void init() throws ExportException {
        if (logger.isDebugEnabled())
            logger.debug("loading freemarker settings");
        try {
            freemarker.log.Logger.selectLoggerLibrary(LIBRARY_LOG4J);
        } catch (ClassNotFoundException cnfe) {
            throw new ExportException(cnfe);
        }
    }

    public void exportData(final ExportParameters parameters) throws ExportException {
        if (parameters == null) {
            throw new IllegalArgumentException("cannot perform export with " + "null parameters");
        }
        if (parameters.getFormatType() == null) {
            throw new IllegalArgumentException("cannot perform export without" + " an export type set");
        }

        long start = System.currentTimeMillis();

        if (logger.isDebugEnabled()) {
            logger.debug("exporting based on filter " + parameters);
        }

        List<Translation> translations = transferRepository.findTranslations(parameters);

        Exporter exporter = exporterFactory.getExporter(parameters.getFormatType());
        exporter.exportData(parameters, preprocessTranslations(parameters, translations));

        if (parameters.isResultPackaged()) {
            createArchive(parameters.getOutputPath());
        }

        if (logger.isInfoEnabled()) {
            float totalMillis = System.currentTimeMillis() - start;
            logger.info("export complete in " + (totalMillis / 1000) + " seconds");
        }
    }

    /**
     * TFS 18781: Add placeholder translations if there exists no translation
     * for a specific language. Placeholders are only generated if
     * {@link ExportParameters#isPlaceholder()} is true.
     * 
     * Placeholder contains locale key and default language text: e.g.
     * "messagekey=fr_Hier ist nur der deutsche Text vorhanden"
     * 
     * If default language translation is also missing, the placeholder contains
     * locale key and message property key: e.g. "messagekey=fr_messagekey"
     * 
     * @param parameters
     *            export parameters
     * @param translations
     *            list of translation to search for missing language translation
     * @return list of translations containing placeholder translations or
     *         original list if parameter
     *         {@link ExportParameters#isPlaceholder()} was set to false
     */
    private List<Translation> preprocessTranslations(final ExportParameters parameters, List<Translation> translations) {
        if (!parameters.isPlaceholder()) {
            return translations;
        }

        // get default language, country and their translations
        Language defaultLanguage = keywordService.getLanguage(LanguageCode.DEFAULT);
        Country defaultCountry = keywordService.getCountry(CountryCode.DEFAULT);

        ExportParameters defaultParameters = new ExportParameters();
        defaultParameters.setBundles(parameters.getBundles());
        defaultParameters.setCountries(Arrays.asList(defaultCountry));
        defaultParameters.setFormatType(parameters.getFormatType());
        defaultParameters.setTranslationStates(parameters.getTranslationStates());
        defaultParameters.setLanguages(Arrays.asList(defaultLanguage));

        List<Translation> defaultTranslations = transferRepository.findTranslations(defaultParameters);

        // create new result list with already existing translations
        List<Translation> result = new ArrayList<Translation>(translations.size());
        result.addAll(translations);

        // check if translation exists for each language of each translation
        for (Translation translation : translations) {
            for (Language language : parameters.getLanguages()) {

                TranslationKeywordPredicate predicate = new TranslationKeywordPredicate(translation.getKeyword().getKeyword(), translation
                        .getBundle(), translation.getCountry(), language);

                if (!CollectionUtils.exists(result, predicate)) {

                    // no translation exists for language, create new with
                    // default translation value
                    TranslationKeywordPredicate defaultPredicate = new TranslationKeywordPredicate(translation.getKeyword().getKeyword(),
                            translation.getBundle(), defaultCountry, defaultLanguage);

                    Translation clone = translation.deepClone();
                    clone.setLanguage(language);

                    // set default language/country translation or key if none
                    // exists
                    if (CollectionUtils.exists(defaultTranslations, defaultPredicate)) {
                        Translation defaultTranslation = (Translation) CollectionUtils.find(defaultTranslations, defaultPredicate);
                        StringBuffer newValue = new StringBuffer();
                        newValue.append(language.getCode().name()).append("_");
                        newValue.append(defaultTranslation.getValue());
                        clone.setValue(newValue.toString());
                    } else {
                        StringBuffer newValue = new StringBuffer();
                        newValue.append(language.getCode().name()).append("_");
                        newValue.append(translation.getKeyword().getKeyword());
                        clone.setValue(newValue.toString());
                    }
                    result.add(clone);
                }
            }

        }
        return result;
    }

    private void createArchive(String outputPath) throws ExportException, IllegalArgumentException {
        File directory = new File(outputPath);

        if (!directory.isDirectory())
            throw new IllegalArgumentException("expecting a directory");

        ZipOutputStream zos = null;
        try {
            File[] files = directory.listFiles();
            if (files.length > 0) {
                final File archive = new File(directory, directory.getName() + ".zip");
                zos = new ZipOutputStream(new FileOutputStream(archive));
                for (File file : files) {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    IOUtils.write(FileUtils.readFileToByteArray(file), zos);
                    zos.closeEntry();
                }

                if (logger.isDebugEnabled())
                    logger.debug("archived " + files.length + " files to " + archive.getPath());
            }
        } catch (IOException ioe) {
            throw new ExportException(ioe);
        } finally {
            IOUtils.closeQuietly(zos);
        }
    }

    public void importData(final ImportParameters parameters) {
        final long start = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("importing based on filter " + parameters);
        }

        Importer importer = importerFactory.getImporter(parameters.getFormatType());
        importer.importData(parameters);

        if (logger.isInfoEnabled()) {
            final long totalMillis = System.currentTimeMillis() - start;
            logger.info("import complete in " + (totalMillis / 1000) + " seconds");
        }
    }
}
