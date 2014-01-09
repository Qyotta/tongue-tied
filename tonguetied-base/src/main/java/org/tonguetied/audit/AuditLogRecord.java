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
package org.tonguetied.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

/**
 * Stateful object that contains a summary of a database transaction.
 * 
 * @author bsion
 *
 */
@Entity
@NamedQueries({
    @NamedQuery(name=AuditLogRecord.QUERY_AUDIT_LOG_RECORD_COUNT,
        query="select count(*) from AuditLogRecord"),
    @NamedQuery(name=AuditLogRecord.QUERY_GET_AUDIT_LOG,
        query="from AuditLogRecord alr order by alr.created desc")
})
@Immutable
@Table(name=AuditLogRecord.TABLE_AUDIT_LOG_RECORD)
public class AuditLogRecord
{
    private Long id;
    private Operation message;
    private Long entityId;
    private Class<?> entityClass;
    private String oldValue;
    private String newValue;
    private String username;
    private Date created;
    
    public static final String TABLE_AUDIT_LOG_RECORD = "audit_log_record";
    private static final String COL_ID = TABLE_AUDIT_LOG_RECORD + "_id";
    public static final String QUERY_GET_AUDIT_LOG = "get.audit.log";
    public static final String QUERY_AUDIT_LOG_RECORD_COUNT = 
        "audit.log.record.count";
    
    AuditLogRecord()
    {
    }

    /**
     * Create a new instance of the AuditLogRecord. The created date is set to
     * the current date.
     * 
     * @param message a string indicating the type of operation performed
     * @param entity the object that was sent to persistence
     * @param newValue the updated object
     * @param oldValue the object before being updated
     * @param username the username of the user who made the change
     */
    public AuditLogRecord(final Operation message,
                          final Auditable entity,
                          final String newValue,
                          final String oldValue,
                          final String username)
    {
        this.message = message;
        this.entityId = entity.getId();
        this.entityClass = entity.getClass();
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.username = username;
        this.created = new Date();
    }

    /**
     * @return the the unique identifier for this record
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator="audit_log_record_generator")
    @SequenceGenerator(name="audit_log_record_generator",sequenceName="audit_log_record_id_seq")
    @Column(name=COL_ID)
    public Long getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the message describing the action taken
     */
    @Column(nullable=false,length=7)
    @Enumerated(EnumType.STRING)
    public Operation getMessage()
    {
        return message;
    }

    /**
     * @param message indicates the type of persistence operation being 
     * performed
     */
    public void setMessage(final Operation message)
    {
        this.message = message;
    }

    /**
     * Gets the unique identifier of the entity that was changed.
     * 
     * @return the id of the entity
     */
    @Column(name="entity_id",nullable=false)
    public Long getEntityId()
    {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(final Long entityId)
    {
        this.entityId = entityId;
    }

    /**
     * @return the entityClass
     */
    @Column(name="entity_class", nullable=false)
    public Class<?> getEntityClass()
    {
        return entityClass;
    }

    /**
     * @param entityClass the entityClass to set
     */
    public void setEntityClass(Class<?> entityClass)
    {
        this.entityClass = entityClass;
    }

    /**
     * Gets the username of the <code>User</code> who made this change.
     * 
     * @return the username of the user who made the change, or <tt>null</tt> 
     * if it was done anonymously
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(final String username)
    {
        this.username = username;
    }

    /**
     * Get the value of the object before it was updated at the time point 
     * specified in the {@link #created} date.
     * 
     * @return the original value
     */
    @Column(name="old_value")
    @Type(type="text")
    public String getOldValue()
    {
        return oldValue;
    }

    /**
     * @param oldValue the oldValue to set
     */
    public void setOldValue(final String oldValue)
    {
        this.oldValue = oldValue;
    }

    /**
     * Get the value of the object after it was updated at the time point 
     * specified in the {@link #created} date.
     * 
     * @return the new value
     */
    @Column(name="new_value")
    @Type(type="text")
    public String getNewValue()
    {
        return newValue;
    }

    /**
     * @param newValue the newValue to set
     */
    public void setNewValue(final String newValue)
    {
        this.newValue = newValue;
    }

    /**
     * Returns a cloned instance of the date the record was created. This is 
     * done for security reasons, so that the internals of the date cannot be
     * changed.
     * 
     * @return the date the record was created
     */
    @Column(nullable=false)
    public Date getCreated()
    {
        return (Date) created.clone();
    }

    /**
     * @param created the date the record was created
     */
    public void setCreated(final Date created)
    {
        // assign a copy of this parameter to avoid later changes to mutable 
        // object
        this.created = (Date)created.clone();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        // a good optimization
        if (this == obj)
            isEqual = true;
        else if (obj != null && getClass() == obj.getClass()) 
        {
            final AuditLogRecord other = (AuditLogRecord) obj;
            EqualsBuilder builder = new EqualsBuilder();
            isEqual = builder.append(message, other.message).
                    append(entityId, other.entityId).
                    append(entityClass, other.entityClass).
                    append(username, other.username).
                    append(oldValue, other.oldValue).
                    append(newValue, other.newValue).
                    isEquals();
        }
            
        return isEqual;
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(17, 19);
        int hashCode = builder.append(message).
                    append(newValue).
                    append(oldValue).
                    append(entityId).
                    append(entityClass).
                    append(username).
                    toHashCode();
        
        return hashCode;
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this, 
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
    
    /**
     * Describes different persistence operations that can be performed and we
     * want audited.
     * 
     * @author bsion
     *
     */
    protected enum Operation
    {
        delete, insert, update;
    }
}
