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

import java.util.Collection;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.persister.entity.EntityPersister;

/**
 * Abstract class for audit log listener events.
 * 
 * @author bsion
 *
 */
public abstract class AbstractAuditLogEventListener
{
    private String oldValue;
    private String newValue;

    /**
     * Process all the changed values for an entity's field set. Any unchanged
     * values are not included in the output strings.
     * 
     * @param oldState the previous values of the entity
     * @param newState the current values of the entity
     * @param persister object used to derive entity meta details
     */
    protected final void processEntity(final Object[] oldState,
            final Object[] newState, final EntityPersister persister)
    {
        final String[] names = persister.getPropertyNames();
        StringBuilder oldBuilder = new StringBuilder();
        StringBuilder newBuilder = new StringBuilder();
        for (int i =0; i < names.length; i++)
        {
            if (!isOptlockField(persister, names[i]) && 
                (oldState == null || newState == null || 
                    !ObjectUtils.equals(oldState[i], newState[i])))
            {
                {
                    if (oldState != null)
                    {
                        oldBuilder.append(names[i]).append(" = ").
                            append(evaluateString(oldState[i])).append("\n");
                    }
                    if (newState != null)
                    {
                        newBuilder.append(names[i]).append(" = ").
                            append(evaluateString(newState[i])).append("\n");
                    }
                }
            }
        }
        
        oldValue = oldState == null? null: oldBuilder.toString();
        newValue = newState == null? null: newBuilder.toString();
    }

    /**
     * Determine the string value to output
     * 
     * @param state the field type to process
     * @return a string representation of the field type
     */
    private String evaluateString(final Object state)
    {
        final String value;
        if (state instanceof Auditable)
        {
            value = ((Auditable) state).toLogString();
        }
        else if (state instanceof Collection)
        {
            StringBuilder builder = new StringBuilder();
            for (Object item : (Collection<?>)state)
            {
                builder.append(evaluateString(item)).append("\n");
            }
            value = "".equals(builder.toString())? null: builder.toString();
        }
        else if (state instanceof AuditSupport)
        {
            value = ((AuditSupport) state).toLogString();
        }
        else
        {
            value = state == null? null: state.toString();
        }
        
        return value;
    }

    /**
     * Determine if this field is used for optimistic locking.
     * 
     * @param persister
     * @param name
     * @return <code>true</code> if the field is used for optimistic locking,
     * <code>false</code> otherwise
     */
    private boolean isOptlockField(EntityPersister persister,  final String name)
    {
        return persister.isVersioned() && "version".equals(name);
    }

    /**
     * The string representation of the previous changed values. Before calling
     * this method, the 
     * {@link #processEntity(Object[], Object[], EntityPersister)}
     * method needs to be called.
     * 
     * @return the oldValue string
     * @see #processEntity(Object[], Object[], EntityPersister)
     */
    protected final String getOldValue()
    {
        return oldValue;
    }

    /**
     * The string representation of the updated changed values. Before calling
     * this method, the 
     * {@link #processEntity(Object[], Object[], EntityPersister)}
     * method needs to be called.
     * 
     * @return the newValue string
     * @see #processEntity(Object[], Object[], EntityPersister)
     */
    protected final String getNewValue()
    {
        return newValue;
    }
}
