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
package org.tonguetied.administration;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;

/**
 * Stateful object to store the system installation details.
 * 
 * @author bsion
 *
 */
@Entity
@AccessType("field")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name=ServerData.TABLE_SERVER_DATA,uniqueConstraints={@UniqueConstraint(columnNames={"version",ServerData.COL_BUILD_NUMBER})})
@NamedQuery(name=ServerData.QUERY_GET_LATEST_SERVER_DATA,query="from ServerData sd where sd.buildDate = (select max(sd2.buildDate) from ServerData sd2)")
@Immutable
public class ServerData implements Comparable<ServerData>
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator="server_data_generator")
    @SequenceGenerator(name="server_data_generator",sequenceName="server_data_id_seq")
    @Column(name=COL_ID)
    private Long id;
    @Column(nullable=false,length=10)
    private String version;
    @Column(name=COL_BUILD_NUMBER,nullable=false,length=10)
    private String buildNumber;
    @Column(name="build_date",nullable=false)
    private Date buildDate;
    @Column(name="setup_date",nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date setupDate;
    
    public static final String TABLE_SERVER_DATA = "server_data";
    private static final String COL_ID = TABLE_SERVER_DATA + "_id";
    public static final String COL_BUILD_NUMBER = "build_number";
    
    protected static final String QUERY_GET_LATEST_SERVER_DATA = 
        "get.latest.server.data";
    
    /**
     * Create a new instance of ServerData.
     *
     */
    ServerData()
    {
    }

    /**
     * Create a new instance of ServerData.
     * 
     * @param version the version of the application
     * @param buildNumber the build number of the application
     * @param buildDate the date this application was built
     */
    public ServerData(final String version, final String buildNumber, 
            final Date buildDate)
    {
        // assign a copy of this parameter to avoid later changes to mutable 
        // object
        this.buildDate = (Date)buildDate.clone();
        this.buildNumber = buildNumber;
        this.version = version;
        this.setupDate = new Date();
    }

    /**
     * @return the id
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Assign the id.
     *
     * @param id the id to set
     */
    void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the version
     */
    public String getVersion()
    {
        return version;
    }
    
    /**
     * Assign the version.
     *
     * @param version the version to set
     */
    void setVersion(String version)
    {
        this.version = version;
    }
    
    /**
     * @return the buildNumber
     */
    public String getBuildNumber()
    {
        return buildNumber;
    }
    
    /**
     * Assign the buildNumber.
     *
     * @param buildNumber the buildNumber to set
     */
    void setBuildNumber(String buildNumber)
    {
        this.buildNumber = buildNumber;
    }
    
    /**
     * @return the buildDate
     */
    public Date getBuildDate()
    {
        return buildDate;
    }
    
    /**
     * Assign the buildDate.
     *
     * @param buildDate the buildDate to set
     */
    void setBuildDate(Date buildDate)
    {
        this.buildDate = buildDate;
    }
    
    /**
     * @return the setupDate
     */
    public Date getSetupDate()
    {
        return setupDate;
    }
    
    /**
     * Assign the setupDate.
     *
     * @param setupDate the setupDate to set
     */
    void setSetupDate(Date setupDate)
    {
        this.setupDate = setupDate;
    }
    

    /**
     * Determine if this instance of the {@link ServerData} is older or newer
     * than the version passed in.
     */
    public int compareTo(ServerData other)
    {
        return new CompareToBuilder().append(version, other.version).toComparison();
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(31, 29);
        builder.append(version).append(buildNumber).append(buildDate);

        return builder.toHashCode();
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
        else if (obj instanceof ServerData)
        {
            final ServerData other = (ServerData)obj;
            
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(version, other.version).
                append(buildNumber, other.buildNumber).
                append(buildDate, other.buildDate);
            
            isEqual = builder.isEquals();
        }
        
        return isEqual;
    }
    
    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this, 
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
}
