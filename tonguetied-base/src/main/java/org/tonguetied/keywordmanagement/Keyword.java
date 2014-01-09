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
package org.tonguetied.keywordmanagement;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.hibernate.annotations.Type;
import org.tonguetied.audit.Auditable;
import org.tonguetied.keywordmanagement.Translation.TranslationIdComparator;
import org.tonguetied.utils.pagination.DeepCloneable;

/**
 * A <code>Keyword</code> object maps a relationship between a list of 
 * {@link Translation}s a key string.
 * 
 * @author mforslund
 * @author bsion
 */
@Entity
@AccessType("property")
@NamedQueries({
    @NamedQuery(name=Keyword.QUERY_KEYWORD_COUNT,query="select count(*) from Keyword"),
    @NamedQuery(name=Keyword.QUERY_GET_KEYWORDS,query="from Keyword k order by k.keyword asc"),
    @NamedQuery(name=Keyword.QUERY_GET_KEYWORDS_DESC,query="from Keyword k order by k.keyword desc")
})
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name=Keyword.TABLE_KEYWORD)
public class Keyword implements DeepCloneable<Keyword>, Comparable<Keyword>, 
        Auditable
{
    private Long id;
    private String keyword;
    private String context;
    private SortedSet<Translation> translations;
    
    public static final String TABLE_KEYWORD = "keyword";
    private static final String COL_ID = TABLE_KEYWORD + "_id";
    protected static final String QUERY_GET_KEYWORDS = "get.keywords";
    protected static final String QUERY_GET_KEYWORDS_DESC = "get.keywords.desc";
    protected static final String QUERY_KEYWORD_COUNT = "keyword.count";
    protected static final String FIELD_ID = "id";
    protected static final String FIELD_KEYWORD = "keyword";
    protected static final String FIELD_TRANSLATIONS = "translations";
    
    private static final Logger logger = Logger.getLogger(Keyword.class);
    
    // This attribute is used for optimistic concurrency control in DB
    private Integer version;
    
    public Keyword()
    {
        this.translations = new TreeSet<Translation>();
    }
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator="keyword_generator")
    @SequenceGenerator(name="keyword_generator",sequenceName="keyword_id_seq")
    @Column(name=COL_ID)
    public Long getId()
    {
        return id;
    }
        
    public void setId(final Long id) {
        this.id = id;
    }
    
    /**
     * 
     * @return the key string to which {@link Translation}s are mapped
     */
    @Column(unique=true,nullable=false)
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(final String keyword) {
        this.keyword = keyword;
    }
    
    /**
     * 
     * @return the string describing the use of the {@linkplain Keyword}
     */
    @Type(type="text")
    public String getContext() {
        return context;
    }
    
    public void setContext(final String context) {
        this.context = context;
    }
    
    /**
     * 
     * @return the list of {@link Translation}s mapped to the key string 
     */
    @OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @Sort(type=SortType.NATURAL)
    @JoinColumn(name="KEYWORD_ID")
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    public SortedSet<Translation> getTranslations()
    {
        return translations;
    }
    
    public void setTranslations(SortedSet<Translation> translations)
    {
        this.translations = translations;
    }
    
    /**
     * Add a {@link Translation} to this <code>Keyword</code>s list of 
     * <code>translations</code>. This method is a convenience method acting as
     * wrapper around the lists' add method. 
     *  
     * @param translation the {@link Translation} to add.
     */
    public void addTranslation(Translation translation)
    {
        if (translation != null && translation.getKeyword() == null)
            translation.setKeyword(this);
        this.translations.add(translation);
    }
    
    /**
     * Remove a {@link Translation} from this Keyword's list of 
     * <code>translations</code>. The translation to be removed is matched by
     * the <code>id</code> of the {@link Translation}. If no match is found,
     * such as before the translation has been persisted, then this method 
     * attempts to remove the translation according to its id in the 
     * translation sets natural order.
     * 
     * @param translationId the id of the {@link Translation}, or the index of
     * the collection, if no id exists
     */
    public void removeTranslation(final Long translationId)
    {
        if (translationId == null)
            throw new IllegalArgumentException("translationId cannot be null");
            
        Translation translation = (Translation) CollectionUtils.find(
                translations, new TranslationIdPredicate(translationId));
        if (translation == null)
        {
            Translation[] array = new Translation[this.translations.size()];
            this.translations.toArray(array);
            translation = array[translationId.intValue()];
        }
        
        remove(translation);
    }
    
    /**
     * Remove a {@link Translation} from this <code>Keyword</code>s list of 
     * <code>translations</code>. This method is a convenience method acting as
     * a wrapper around the lists' remove method. 
     * 
     * @param translation the {@link Translation} to remove.
     */
    public void remove(Translation translation)
    {
        if (translation.getId() != null)
        {
            SortedSet<Translation> tempSet = new TreeSet<Translation>(
                    new TranslationIdComparator());
            tempSet.addAll(this.translations);
            this.translations = tempSet;
        }
        
        if (this.translations.remove(translation))
        {
            translation.setKeyword(null);
        }
        else
        {
            logger.warn("failed to remove translation: " + translation);
        }
    }
    
    /**
     * This field is used for optimistic locking.
     * 
     * @return the version
     */
    @Version
    @Column(name="optlock")
    public Integer getVersion()
    {
        return version;
    }

    /**
     * This field is used for optimistic locking.
     * 
     * @param version the version to set
     */
    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public int compareTo(final Keyword other)
    {
        return new CompareToBuilder().append(keyword, other.keyword).
                append(context, other.context).
                toComparison();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        // a good optimization
        if (this == obj)
        {
            isEqual = true;
        }
        else if (obj instanceof Keyword)
        {
            final Keyword other = (Keyword)obj;
            
            isEqual = (this.keyword == null? 
                        other.keyword == null: keyword.equals(other.keyword))
                    && (this.context == null?
                        other.context == null: context.equals(other.context))
                    && (this.translations == null?
                        other.translations == null:
                            SetUtils.isEqualSet(translations, other.translations));
//                            translations.equals(other.translations));
        }
        
        return isEqual;
    }
    
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(23, 17);
        return builder.append(keyword).append(context).toHashCode();
    }
    
    /**
     * Performs a deep copy of this keyword object.
     */
    public final Keyword deepClone()
    {
        Keyword clone;
        try
        {
            clone = (Keyword) super.clone();
            if (translations != null) {
                clone.setTranslations(new TreeSet<Translation>());
                for (Translation translation: translations)
                {
                    clone.addTranslation(translation.deepClone());
                }
            }
        }
        catch (CloneNotSupportedException cnse)
        {
            clone = null;
        }
        return clone;
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this, 
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
    
    public String toLogString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Keyword[keyword=").append(keyword).append(",context=").
        append(context).append(",translations=[");
        for (Translation translation : translations)
        {
            builder.append("\nbundle=");
            builder.append(translation.getBundle() == null? null:
                translation.getBundle().toLogString());
            builder.append(", country=");
            builder.append(translation.getCountry() == null? null:
                translation.getCountry().toLogString());
            builder.append(", language=");
            builder.append(translation.getLanguage() == null? null:
                translation.getLanguage().toLogString());
            builder.append(", state=");
            builder.append(translation.getState());
            builder.append(", value=");
            builder.append(translation.getValue());
        }
        builder.append("]]");
        
        return builder.toString();
    }
}
