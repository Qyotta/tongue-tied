package org.tonguetied.datatransfer.exporting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.KeywordByLanguage;
import org.tonguetied.keywordmanagement.KeywordService;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;

/**
 * A freemarker template based implementation of {@link Exporter} that adds
 * language centric data to the model.
 */
public class FreemarkerLanguageCentricExporter extends FreemarkerExporter {

    private KeywordService keywordService;

    public void setKeywordService(KeywordService keywordService) {
        this.keywordService = keywordService;
    }

    @Override
    protected Map<String, Object> createModel(ExportParameters parameters, List<Translation> data) {
        Map<String, Object> model = super.createModel(parameters, data);

        // Override translations.
        model.put("translations", transformData(data));

        // Add languages
        List<Language> languages = parameters.getLanguages();
        Collections.sort(languages);
        model.put("languages", languages);

        return model;
    }

    private List<KeywordByLanguage> transformData(List<Translation> translations) {
        Country defaultCountry = keywordService.getCountry(CountryCode.DEFAULT);

        List<KeywordByLanguage> results = new ArrayList<KeywordByLanguage>();
        if (translations != null) {
            for (Translation translation : translations) {
                LanguageCode languageCode = null;
                if (CountryCode.TW == translation.getCountry().getCode()) {
                    translation.setCountry(defaultCountry);
                    languageCode = LanguageCode.zht;
                }
                KeywordByLanguage item = findItem(results, translation);
                if (item == null) {
                    item = new KeywordByLanguage(translation.getKeyword().getKeyword(), translation.getKeyword().getContext(), translation
                            .getBundle(), translation.getCountry());
                    results.add(item);
                }
                item.addTranslation(languageCode != null ? languageCode : translation.getLanguage().getCode(), translation
                        .getCorrectedValue());
            }
        }

        return results;
    }

    /**
     * Find an existing {@link KeywordByLanguage} item based on criteria
     * provided in the <code>translation<code> parameter. If no match is 
     * found <code>null</code> is returned.
     * 
     * @param results
     *            the list of {@link KeywordByLanguage} items to search
     * @param translation
     *            the {@link Translation} object used to construct the search
     *            criteria
     * @return the first item matching the criteria, or <code>null</code> if no
     *         match is found
     * 
     * @see LanguagePredicate
     */
    private KeywordByLanguage findItem(List<KeywordByLanguage> results, Translation translation) {
        Predicate predicate = new LanguagePredicate(translation.getKeyword().getKeyword(), translation.getKeyword().getContext(),
                translation.getBundle(), translation.getCountry());

        return (KeywordByLanguage) CollectionUtils.find(results, predicate);
    }

    /**
     * Filter to determine if two {@link KeywordByLanguage} objects are equal
     * based on all attributes of the Predicate.
     * 
     * @author bsion
     * 
     */
    private static class LanguagePredicate implements Predicate {
        private String keyword;
        private String context;
        private Bundle bundle;
        private Country country;

        /**
         * Create a new instance of LanguagePredicate.
         * 
         * @param keyword
         *            the {@link Keyword} keyword to match
         * @param context
         *            the {@link Keyword} context to match
         * @param bundle
         *            the {@link Bundle} to match
         * @param country
         *            the {@link Country} to match
         */
        public LanguagePredicate(final String keyword, final String context, final Bundle bundle, final Country country) {
            this.keyword = keyword;
            this.context = context;
            this.bundle = bundle;
            this.country = country;
        }

        public boolean evaluate(Object object) {
            KeywordByLanguage item = (KeywordByLanguage) object;
            EqualsBuilder builder = new EqualsBuilder();
            return builder.append(keyword, item.getKeyword()).append(context, item.getContext()).append(bundle, item.getBundle()).append(
                    country, item.getCountry()).isEquals();
        }
    }
}
