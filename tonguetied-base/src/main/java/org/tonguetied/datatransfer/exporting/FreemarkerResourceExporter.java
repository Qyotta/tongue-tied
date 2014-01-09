package org.tonguetied.datatransfer.exporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.TranslationKeywordPredicate;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;

/**
 * A freemarker template based implementation of {@link Exporter} that exports
 * each resource bundle to locale specific files.
 */
public class FreemarkerResourceExporter extends FreemarkerExporter {
    @Override
    public void exportData(ExportParameters parameters, List<Translation> data) throws ExportException {
        if (parameters.getOutputResourceName() != null) {
            // If a resource name is specified, we merge all given bundles. This
            // option is used by the maven plugin for example.
            exportResourceForEachLocale(parameters, data, parameters.getOutputResourceName());
        } else {
            // If no resource name is specified, we export each non global
            // bundle.
            for (Entry<Bundle, List<Translation>> translationsByBundle : mapByBundles(parameters, data).entrySet()) {
                exportResourceForEachLocale(parameters, translationsByBundle.getValue(), translationsByBundle.getKey().getResourceName());
            }
        }
    }

    private void exportResourceForEachLocale(ExportParameters parameters, List<Translation> data, String resourceName) {
        for (Entry<Locale, List<Translation>> translationsByLocale : mapByLocale(data).entrySet()) {
            Locale locale = translationsByLocale.getKey();
            List<Translation> translations = translationsByLocale.getValue();

            if (!translations.isEmpty()) {
                String outputFileName = resourceName + localeFileSuffix(locale) + "." + parameters.getFormatType().getFileExtension();
                processTemplate(createModel(parameters, translations), parameters.getOutputPath(), outputFileName);
            }
        }
    }

    private String localeFileSuffix(Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (language.length() > 0) {
            return "_" + language + (country.length() > 0 ? "_" + country : "");
        }
        return "";
    }

    /**
     * Maps the translations by their bundle. Merges global bundles if the
     * parameter is set, i.e. includes translations in global bundles into each
     * selected bundle. If two translations exist with different global bundles,
     * then only one of the translations will be added. This will be process on
     * first occurrence.
     * 
     * @param parameters
     *            the parameters
     * @param translations
     *            the {@link Translation}s to process
     * @return a map from bundle to a list of translation
     */
    private Map<Bundle, List<Translation>> mapByBundles(ExportParameters parameters, List<Translation> translations) {
        Map<Bundle, List<Translation>> result = new HashMap<Bundle, List<Translation>>();

        List<Bundle> bundles = parameters.isGlobalsMerged() ? collectNonGlobalBundles(parameters.getBundles()) : parameters.getBundles();
        for (Bundle bundle : bundles) {
            result.put(bundle, new ArrayList<Translation>());
        }

        for (Translation translation : translations) {
            if (parameters.isGlobalsMerged() && translation.getBundle().isGlobal()) {
                for (Entry<Bundle, List<Translation>> translationsByBundle : result.entrySet()) {
                    TranslationKeywordPredicate predicate = new TranslationKeywordPredicate(translation.getKeyword().getKeyword(),
                            translationsByBundle.getKey(), translation.getCountry(), translation.getLanguage());
                    // If the translation for that keyword does not yet exist
                    // then add
                    if (!CollectionUtils.exists(translationsByBundle.getValue(), predicate)) {
                        Translation clone = translation.deepClone();
                        clone.setBundle(translationsByBundle.getKey());
                        translationsByBundle.getValue().add(clone);
                    }
                }
            } else {
                if (result.containsKey(translation.getBundle())) {
                    result.get(translation.getBundle()).add(translation);
                }
            }
        }

        return result;
    }

    /**
     * Find all the non global bundles from input bundle list.
     * 
     * @param bundles
     *            the list of bundles to process
     * @return a list of non-global bundles
     */
    private List<Bundle> collectNonGlobalBundles(final List<Bundle> bundles) {
        List<Bundle> regulars = new ArrayList<Bundle>();

        for (final Bundle bundle : bundles) {
            if (!bundle.isGlobal())
                regulars.add(bundle);
        }

        return regulars;
    }

    private Map<Locale, List<Translation>> mapByLocale(List<Translation> translations) {
        Map<Locale, List<Translation>> result = new HashMap<Locale, List<Translation>>();
        for (Translation translation : translations) {
            Locale locale = createLocale(translation.getLanguage().getCode(), translation.getCountry().getCode());

            List<Translation> translationsForLocale = result.get(locale);
            if (translationsForLocale == null) {
                translationsForLocale = new ArrayList<Translation>();
                result.put(locale, translationsForLocale);
            }

            translationsForLocale.add(translation);
        }

        return result;
    }

    private Locale createLocale(LanguageCode languageCode, CountryCode countryCode) {
        String language = languageCode == LanguageCode.DEFAULT ? "" : languageCode.name().toLowerCase();
        String country = countryCode == CountryCode.DEFAULT ? "" : countryCode.name().toUpperCase();
        return new Locale(language, country);
    }
}
