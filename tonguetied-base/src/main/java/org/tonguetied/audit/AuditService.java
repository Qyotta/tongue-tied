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

import java.util.List;

import org.tonguetied.utils.pagination.PaginatedList;


/**
 * Interface defining the entry points to the Audit features
 * 
 * @author bsion
 *
 */
public interface AuditService
{
    /**
     * Retrieve a list of all {@link AuditLogRecord}s in the system. 
     * 
     * @return a list of all {@link AuditLogRecord} in the system
     */
    List<AuditLogRecord> getAuditLog();

    /**
     * Get all the {@link AuditLogRecord}s in the system.
     * 
     * @param firstResult a row number, numbered from 0. If <code>null</code>
     * then then results begin at zero. If the value is negative, then the 
     * results begin at zero
     * @param maxResults the maximum number of rows. If <code>null</code> then
     * all results are returned
     * @return all {@link AuditLogRecord}s in the system.
     */
    PaginatedList<AuditLogRecord> getAuditLog(final Integer firstResult,
            final Integer maxResults);
}
