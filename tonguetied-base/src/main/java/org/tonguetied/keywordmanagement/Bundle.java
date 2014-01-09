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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.tonguetied.audit.AuditSupport;

/**
 * Class describing a resource bundle as it relates to resource management. The
 * bundle is one aspect used to determine a {@link Translation}.
 * 
 * @author bsion
 */
@Entity
@AccessType("field")
@NamedQueries({
    @NamedQuery(name=Bundle.QUERY_GET_BUNDLES, query="from Bundle b order by b.name"),
    @NamedQuery(name=Bundle.QUERY_GET_DEFAULT_BUNDLE, query="from Bundle b where b.isDefault = true"),
    @NamedQuery(name=Bundle.QUERY_FIND_BUNDLES, query="from Bundle b where b.name = :name or b.resourceName = :resourceName")
    })
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name=Bundle.TABLE_BUNDLE)
public class Bundle implements Comparable<Bundle>, AuditSupport
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator="bundle_generator")
    @SequenceGenerator(name="bundle_generator",sequenceName="bundle_id_seq")
    @Column(name=COL_ID)
    private Long id;
    @Column(unique=true,nullable=false)
    private String name;
    private String description;
    @Column(name="resource_name",unique=true,nullable=false)
    private String resourceName;
    /**
     * Only one bundle will have a value of true
     */
    @Column(name="is_default",nullable = false)
    private boolean isDefault;
    /**
     * Flag indicating this is a global bundle. Any entries marked as global
     * should be included in all exports.
     */
    @Column(name="is_global",nullable = false)
    private boolean isGlobal;

    public static final String FIELD_NAME = "name";
    public static final String FIELD_RESOURCE_NAME = "resourceName";
    public static final String TABLE_BUNDLE = "bundle";
    private static final String COL_ID = TABLE_BUNDLE + "_id";
    protected static final String QUERY_GET_DEFAULT_BUNDLE = "get.default.bundle";
    protected static final String QUERY_GET_BUNDLES = "get.bundles";
    protected static final String QUERY_FIND_BUNDLES = "find.bundles";

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getResourceName()
    {
        return resourceName;
    }

    /**
     * Set the resource bundle name.
     * 
     * @param resourceName the name of the resource bundle.
     */
    public void setResourceName(final String resourceName)
    {
        this.resourceName = resourceName;
    }

    public boolean isDefault()
    {
        return isDefault;
    }

    public void setDefault(final boolean isDefault)
    {
        this.isDefault = isDefault;
    }

    /**
     * @return the flag indicating if {@link Translation}s associated with
     *         Bundle should be in considered part of every bundle.
     */
    public boolean isGlobal()
    {
        return isGlobal;
    }

    /**
     * @param isGlobal the isGlobal to set
     */
    public void setGlobal(final boolean isGlobal)
    {
        this.isGlobal = isGlobal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Bundle other)
    {
        return new CompareToBuilder().append(name, other.name).append(
                resourceName, other.resourceName).append(isGlobal,
                other.isGlobal).append(isDefault, other.isDefault)
                .toComparison();
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
        else if (obj instanceof Bundle)
        {
            final Bundle other = (Bundle) obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(this.description, other.description).append(
                    this.name, other.name).append(this.resourceName,
                    other.resourceName).append(this.isGlobal, other.isGlobal)
                    .append(this.isDefault, other.isDefault);
            isEqual = builder.isEquals();
        }

        return isEqual;
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(19, 11);
        builder.append(resourceName).append(name).append(description).append(
                isGlobal).append(isDefault);

        return builder.toHashCode();
    }

    public String toLogString()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this,
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
