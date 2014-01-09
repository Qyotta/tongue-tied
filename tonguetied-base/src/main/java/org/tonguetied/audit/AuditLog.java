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

import org.apache.log4j.Logger;
import org.hibernate.CallbackException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.tonguetied.audit.AuditLogRecord.Operation;


/**
 * Utility class used to persist audit log records.
 * 
 * @author bsion
 *
 */
public class AuditLog
{
    private static final Logger logger = Logger.getLogger(AuditLog.class);
    
    /**
     * Create an audit log entry and save in persistent storage.
     * 
     * @param message the message describing the action taken
     * @param entity the object being logged
     * @param newValue the new values of attributes of the entity
     * @param oldValue the previous values of attributes of the entity
     * @param implementor
     * @throws CallbackException
     */
    public static synchronized void logEvent(final Operation message,
            Auditable entity,
            final String newValue,
            final String oldValue,
            SessionFactoryImplementor implementor)
            throws CallbackException 
    {
        Session tempSession = null;
        Transaction tx = null;
        try
        {
            // Use a separate session for saving audit log records
            tempSession = implementor.openSession();
            tx = tempSession.beginTransaction();

            tx.begin();
            final AuditLogRecord record = new AuditLogRecord(
                        message, entity, newValue, oldValue, getUsername());
            
            tempSession.save(record);
            tx.commit();
            if (logger.isDebugEnabled())
                logger.debug("successfully saved audit log record: " + record);
        }
        catch (HibernateException ex)
        {
            if (logger.isInfoEnabled())
                logger.info("rolling back transation ");
            if (tx != null) tx.rollback();
            throw new CallbackException(ex);
        }
        finally
        {
            if (tempSession != null)
                tempSession.close();
        }
    }
    
    /**
     * Gets the current user name from the Spring Security SecurityContext.
     * 
     * @return current user, or <tt>null</tt> if no user is currently logged in
     */
    private static synchronized String getUsername()
    {
        final SecurityContext secureContext = SecurityContextHolder.getContext();

        String username = null;
        final Authentication auth = secureContext.getAuthentication();

        if (auth.getPrincipal() instanceof UserDetails)
        {
            username = ((UserDetails) auth.getPrincipal()).getUsername();
        }
        
        return username;
    }
}
