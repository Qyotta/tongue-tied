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
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.tonguetied.audit.AuditLogRecord.Operation;


/**
 * Event listener to add a new audit log record before an insert to an 
 * {@link Auditable} class has been made.
 *  
 * @author bsion
 *
 */
public class AuditLogPostInsertEventListener extends AbstractAuditLogEventListener
        implements PostInsertEventListener
{
    private static final Logger logger = 
        Logger.getLogger(AuditLogPostInsertEventListener.class);

    private static final long serialVersionUID = -8154917326845979720L;

    /**
     * Add an entry in the audit log for the creation of an {@link Auditable} 
     * entity.
     */
    public void onPostInsert(final PostInsertEvent event)
    {
        if (event.getEntity() instanceof Auditable)
        {
            final Auditable entity = (Auditable)event.getEntity();
            if (logger.isDebugEnabled())
                logger.debug("Adding an audit log entry for insertion of entity "
                        + entity.getClass() + " with id " + entity.getId());
            final Object[] state = event.getState();
            final EntityPersister persister = event.getPersister();
            processEntity(null, state, persister);
            
            AuditLog.logEvent(Operation.insert, entity, getNewValue(), 
                getOldValue(), persister.getFactory());
        }
    }
}
