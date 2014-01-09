package org.tonguetied.maven.internal;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.tonguetied.datatransfer.DataService;
import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.datatransfer.common.FormatType;
import org.tonguetied.datatransfer.exporting.ExportException;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.BundleRepository;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.CountryRepository;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.LanguageRepository;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;
import org.tonguetied.maven.Resource;


import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Main class which will process the export service
 */
public class ExportService {

    /**
     * The {@link DataService export service}
     */
    private DataService dataService;

    /**
     * The {@link BundleRepository}
     */
    private BundleRepository bundleRepository;

    /**
     * The {@link CountryRepository}
     */
    private CountryRepository countryRepository;

    /**
     * The {@link LanguageRepository}
     */
    private LanguageRepository languageRepository;

    /**
     * The {@link ComboPooledDataSource}
     */
    private ComboPooledDataSource poolDataSource;

    /**
     * The {@link Log logger}
     */
    private Log logger;

    /**
     * Main constructor.
     * 
     * @param logger
     *            the {@link Log logger}
     */
    public ExportService(Log logger) {
        this.logger = logger;
    }

    /**
     * Initialize the Spring context
     * 
     * @param springConfigFile
     *            the spring config file location
     * @param jdbcUrl
     *            the jdbc url (optional)
     * @param jdbcUser
     *            the jdbc user (optional)
     * @param jdbcPassword
     *            the jdbc password (optional)
     */
    public void initSpringContext(String springConfigFile, String jdbcUrl, String jdbcUser, String jdbcPassword) {
        logger.debug("Initializing spring context");
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(springConfigFile);
        dataService = (DataService) ctx.getBean("dataService");
        bundleRepository = (BundleRepository) ctx.getBean("bundleRepository");
        countryRepository = (CountryRepository) ctx.getBean("countryRepository");
        languageRepository = (LanguageRepository) ctx.getBean("languageRepository");
        poolDataSource = (ComboPooledDataSource) ctx.getBean("dataSource");

        updateJdbcConnection(jdbcUrl, jdbcUser, jdbcPassword);

        logger.debug("End of spring context initialization");
    }

    /**
     * Update the DataSource if needed
     * 
     * @param jdbcUrl
     *            the new optional jdbc URL
     * @param jdbcUser
     *            the new optional jdbc User
     * @param jdbcPassword
     *            the new options jdbc Password
     */
    private void updateJdbcConnection(String jdbcUrl, String jdbcUser, String jdbcPassword) {
        if (jdbcUrl != null && jdbcUser != null && jdbcPassword != null) {
            poolDataSource.setJdbcUrl(jdbcUrl);
            poolDataSource.setUser(jdbcUser);
            poolDataSource.setPassword(jdbcPassword);

            poolDataSource.hardReset();
        }
    }

    /**
     * Process the export of given resource
     * 
     * @param resource
     *            the resource to export
     * @throws ExportException
     *             if an error occured during export
     */
    public void processExport(Resource resource) throws ExportException {
        ExportParameters parameters = buildExportParameters(resource);
        logger.debug("Processing export for resource : " + resource.getResourceName());
        dataService.exportData(parameters);
        logger.debug("End of export processing for resource : " + resource.getResourceName());
    }

    /**
     * Build {@link ExportParameters} with the data contained in {@link Resource} object
     * 
     * @param resource
     *            the input {@link Resource} object
     * @return the {@link ExportParameters}
     * @throws ExportException
     *             if an error occured while preparing export parameters
     */
    private ExportParameters buildExportParameters(Resource resource) throws ExportException {
        logger.debug("Build Export parameters");
        ExportParameters parameters = new ExportParameters();
        parameters.setOutputResourceName(resource.getResourceName());
        parameters.setOutputPath(resource.getOutputDirectory());

        // bundles
        for (String currentBundle : resource.getBundles()) {
            Bundle newBundle = bundleRepository.getBundleByResourceName(currentBundle);
            if (newBundle == null) {
                throw new ExportException("Bundle with name \"" + currentBundle + "\" not found in database");
            } else {
                parameters.addBundle(newBundle);
            }
        }

        // format type
        FormatType formatType = getFormatType(resource.getFormatType());
        if (formatType == null) {
            throw new ExportException("Format type \"" + resource.getFormatType() + "\" does not exist");
        }
        parameters.setFormatType(formatType);

        // all countries
        Country defaultCountry = countryRepository.getCountry(CountryCode.DEFAULT);
        if (defaultCountry == null) {
            throw new ExportException("Default country not found in database");
        } else {
            parameters.addCountry(defaultCountry);
        }

        // languages
        List<Language> allLanguages = languageRepository.getLanguages();
        for (String currentLanguageString : resource.getLanguages()) {
            for (Language currentLanguage : allLanguages) {
                if (currentLanguage.getCode().toString().toLowerCase().equals(currentLanguageString.toLowerCase())) {
                    parameters.addLanguage(currentLanguage);
                }
            }
        }
        // translations states
        parameters.setTranslationStates(getTranslationState(resource.getStates()));
        parameters.setGlobalsMerged(true);
        parameters.setPlaceholder(resource.isGeneratePlaceholders());
        parameters.setResultPackaged(false);

        return parameters;
    }

    /**
     * Retourn the corresponding {@link FormatType}
     * 
     * @param formatType
     *            the input format type string
     * @return the {@link FormatType}
     */
    private FormatType getFormatType(String formatType) {
        for (FormatType currentFormatType : FormatType.values()) {
            if (currentFormatType.getFileExtension().toLowerCase().equals(formatType.toLowerCase())) {
                return currentFormatType;
            }
        }
        return null;
    }

    /**
     * Return the list {@link TranslationState} matching with given TranslationStates Strings
     * 
     * @param translationStateStrings
     *            the input TranslationStates Strings
     * @return the list {@link TranslationState}
     */
    @SuppressWarnings("all")
    private List<TranslationState> getTranslationState(String[] translationStateStrings) {
        List<TranslationState> results = new ArrayList<TranslationState>();
        for (String currentTranslationStateString : translationStateStrings) {
            if (currentTranslationStateString.toLowerCase().equals(TranslationState.VERIFIED.toString().toLowerCase())) {
                results.add(TranslationState.VERIFIED);
            } else if (currentTranslationStateString.toLowerCase().equals(TranslationState.QUERIED.toString().toLowerCase())) {
                results.add(TranslationState.QUERIED);
            } else if (currentTranslationStateString.toLowerCase().equals(TranslationState.TRANSIENT.toString().toLowerCase())) {
                results.add(TranslationState.TRANSIENT);
            } else if (currentTranslationStateString.toLowerCase().equals(TranslationState.UNVERIFIED.toString().toLowerCase())) {
                results.add(TranslationState.UNVERIFIED);
            }
        }
        return results;
    }
}
