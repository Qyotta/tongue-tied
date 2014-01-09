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
package org.tonguetied.keywordmanagement;

import java.util.Comparator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.tonguetied.audit.Auditable;
import org.tonguetied.datatransfer.exporting.helper.StringCorrectionHelper;
import org.tonguetied.utils.pagination.DeepCloneable;

/**
 * A translation is a specific value of a {@link Keyword} for a {@link Language}
 * , {@link Country} and {@link Bundle}.
 * 
 * @author mforslund
 * @author bsion
 */
@Entity
@AccessType("property")
@NamedQueries({@NamedQuery(name = Translation.QUERY_FIND_TRANSLATIONS, query = "select translation " + "from Translation translation "
        + "where translation.country in (:countries) " + "and translation.bundle in (:bundles) "
        + "and translation.language in (:languages) " + "and translation.state in (:states) "
        + "order by lower(translation.keyword.keyword), " + "translation.language, " + "translation.country, " + "translation.bundle")})
@Table(name = Translation.TABLE_TRANSLATION, uniqueConstraints = {@UniqueConstraint(columnNames = {"keyword_id", "language_id",
        "country_id", "bundle_id"})})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Translation implements DeepCloneable<Translation>, Comparable<Translation>, Auditable {
    private Long id;
    private String value;
    private Language language;
    private Country country;
    private Bundle bundle;
    private Keyword keyword;
    private TranslationState state;

    // This attribute is used for optimistic concurrency control in DB
    private Integer version;

    public static final String TABLE_TRANSLATION = "translation";
    private static final String COL_ID = TABLE_TRANSLATION + "_id";
    private static final String FK_BUNDLE = "fk_" + Bundle.TABLE_BUNDLE + "_" + TABLE_TRANSLATION;
    private static final String FK_COUNTRY = "fk_" + Country.TABLE_COUNTRY + "_" + TABLE_TRANSLATION;
    private static final String FK_KEYWORD = "fk_" + Keyword.TABLE_KEYWORD + "_" + TABLE_TRANSLATION;
    private static final String FK_LANGUAGE = "fk_" + Language.TABLE_LANGUAGE + "_" + TABLE_TRANSLATION;

    /**
     * Name of the query to search for translations.
     */
    public static final String QUERY_FIND_TRANSLATIONS = "find.translations";

    /**
     * Create a new instance of Translation. This constructor initialises all
     * the required fields.
     */
    public Translation() {
        this.state = TranslationState.UNVERIFIED;
    }

    /**
     * Create a new instance of Translation.
     * 
     * @param bundle
     *            the {@link Bundle} of this Translation
     * @param country
     *            the {@link Country} of this Translation
     * @param language
     *            the {@link Language} of this Translation
     * @param value
     *            the translated value
     * @param state
     *            the state of this Translation
     * @throws IllegalArgumentException
     *             if the <code>state</code> parameter is <code>null</code>
     */
    public Translation(Bundle bundle, Country country, Language language, String value, TranslationState state) {
        if (state == null)
            throw new IllegalArgumentException("state cannot be null");

        this.value = value;
        this.language = language;
        this.country = country;
        this.bundle = bundle;
        this.state = state;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "translation_generator")
    @SequenceGenerator(name = "translation_generator", sequenceName = "translation_id_seq")
    @Column(name = COL_ID)
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bundle_id")
    @ForeignKey(name = FK_BUNDLE)
    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(final Bundle bundle) {
        this.bundle = bundle;
    }

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "country_id")
    @ForeignKey(name = FK_COUNTRY)
    public Country getCountry() {
        return country;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    @ManyToOne
    @JoinColumn(name = "keyword_id", insertable = false, updatable = false)
    @ForeignKey(name = FK_KEYWORD)
    public Keyword getKeyword() {
        return keyword;
    }

    public void setKeyword(final Keyword keyword) {
        this.keyword = keyword;
    }

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "language_id")
    @ForeignKey(name = FK_LANGUAGE)
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(final Language language) {
        this.language = language;
    }

    /**
     * 
     * @return the translated value
     */
    @Type(type = "text")
    public String getValue() {
        return value;
    }

    @Transient
    public String getCorrectedValue() {
        return StringCorrectionHelper.correctString(getValue());
    }

    @Transient
    public String getNlsCorrectedValue() {
        return StringCorrectionHelper.correctStringNLS(getValue());
    }

    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return the flag indicating if the workflow state of this Translation
     */
    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    public TranslationState getState() {
        return state;
    }

    /**
     * @param state
     *            the {@link TranslationState} to set
     */
    public void setState(final TranslationState state) {
        this.state = state;
    }

    /**
     * This field is used for optimistic locking.
     * 
     * @return the version
     */
    @Version
    @Column(name = "optlock")
    public Integer getVersion() {
        return version;
    }

    /**
     * This field is used for optimistic locking.
     * 
     * @param version
     *            the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Translation other) {
        return new CompareToBuilder().append(keyword, other.keyword).append(language, other.language).append(country, other.country)
                .append(bundle, other.bundle).append(value, other.value).append(state, other.state).toComparison();
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        // a good optimization
        if (this == obj) {
            isEqual = true;
        } else if (obj instanceof Translation) {
            final Translation other = (Translation) obj;

            EqualsBuilder builder = new EqualsBuilder();
            isEqual = builder.append(value, other.value).append(bundle, other.bundle).append(country, other.country).append(language,
                    other.language).append(keyword, other.keyword).append(state, other.state).isEquals();
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(33, 19);
        builder.append(keyword).append(bundle).append(country).append(language).append(value).append(state);
        // append(keyword).append(value);

        return builder.toHashCode();
    }

    public Translation deepClone() {
        Translation clone;
        try {
            clone = (Translation) super.clone();
        } catch (CloneNotSupportedException cnse) {
            clone = null;
        }
        return clone;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }

    public String toLogString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Translation[bundle=").append(bundle == null ? null : bundle.toLogString()).append(", country=").append(
                country == null ? null : country.toLogString()).append(", language=").append(
                language == null ? null : language.toLogString()).append(", state=").append(state).append(", value=").append(value).append(
                "]");

        return builder.toString();
    }

    /**
     * These values represent the workflow states for a translation.
     * 
     * @author bsion
     * 
     */
    public static enum TranslationState {
        UNVERIFIED, VERIFIED, QUERIED, TRANSIENT
    }

    /**
     * Comparator class used when Translations should be compared by the
     * {@link Translation#id} rather than the business key as used in the equals
     * method.
     * 
     * @author bsion
     * @see Translation#equals(Object)
     */
    protected static class TranslationIdComparator implements Comparator<Translation> {

        public int compare(Translation t1, Translation t2) {
            int result;
            if (t1.getId() == null && t2.getId() == null) {
                result = 0;
            } else if (t1.getId() != null && t2.getId() == null) {
                result = -1;
            } else if (t1.getId() == null && t2.getId() != null) {
                result = 1;
            } else {
                result = t1.getId().compareTo(t2.getId());
            }
            return result;
        }

    }
}
