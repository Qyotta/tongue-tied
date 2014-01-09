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
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.tonguetied.audit.AuditLogRecord.Operation;


/**
 * Event listener to add a new audit log record before an update to an 
 * {@link Auditable} class has been made. 
 * 
 * @author bsion
 *
 */
public class AuditLogPostUpdateEventListener extends AbstractAuditLogEventListener 
        implements PostUpdateEventListener
{
    private static final Logger logger = 
        Logger.getLogger(AuditLogPostUpdateEventListener.class);

    private static final long serialVersionUID = -6569516811701521612L;

    /**
     * Add an entry in the audit log for the update of an {@link Auditable} 
     * entity.
     */
    public void onPostUpdate(final PostUpdateEvent event)
    {
        if (event.getEntity() instanceof Auditable)
        {
            Auditable entity = (Auditable)event.getEntity();
            if (logger.isDebugEnabled())
                logger.debug("Adding an audit log entry for update to entity "
                        + entity.getClass() + " with id " + entity.getId());
            
            final EntityPersister persister = event.getPersister();
            processEntity(event.getOldState(), event.getState(), persister);
            
//            if (!getNewValue().equals(getOldValue()))
            AuditLog.logEvent(Operation.update, entity, getNewValue(), 
                getOldValue(), persister.getFactory());
        }
    }
}
