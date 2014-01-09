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
 * Concrete implementation of the {@link AuditService} interface.
 * 
 * @author bsion
 *
 */
public class AuditServiceImpl implements AuditService
{
    private AuditRepository auditRepository;

    public List<AuditLogRecord> getAuditLog()
    {
        return getAuditLog(0, null);
    }

    public PaginatedList<AuditLogRecord> getAuditLog(final Integer firstResult,
            final Integer maxResults)
    {
        return auditRepository.getAuditLog(firstResult, maxResults);
    }

    /**
     * @param auditRepository the auditRepository to set
     */
    public void setAuditRepository(final AuditRepository auditRepository)
    {
        this.auditRepository = auditRepository;
    }
}
