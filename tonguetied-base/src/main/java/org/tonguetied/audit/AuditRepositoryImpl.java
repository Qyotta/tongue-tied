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

import static org.tonguetied.audit.AuditLogRecord.QUERY_AUDIT_LOG_RECORD_COUNT;
import static org.tonguetied.audit.AuditLogRecord.QUERY_GET_AUDIT_LOG;

import java.util.List;

import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.tonguetied.utils.pagination.PaginatedList;


/**
 * DAO facade to ORM. This facade allows access to permanent storage of Audit
 * related data via the Hibernate ORM model.
 * 
 * @author bsion
 *
 */
public class AuditRepositoryImpl extends HibernateDaoSupport implements AuditRepository
{
    public PaginatedList<AuditLogRecord> getAuditLog(final Integer firstResult,
            final Integer maxResults)
    {
        Query query = getSession().getNamedQuery(QUERY_GET_AUDIT_LOG);
        if (firstResult != null) query.setFirstResult(firstResult);
        if (maxResults != null) query.setMaxResults(maxResults);
        
        Long maxListSize = 0L;
        final List<AuditLogRecord> queryList = query.list();
        if (queryList.size() > 0)
            maxListSize = (Long) getSession().getNamedQuery(
                    QUERY_AUDIT_LOG_RECORD_COUNT).uniqueResult();
        
        return new PaginatedList<AuditLogRecord>(queryList, maxListSize.intValue());
    }

    public void saveOrUpdate(AuditLogRecord record) throws DataAccessException
    {
        getHibernateTemplate().saveOrUpdate(record);
        getHibernateTemplate().flush();
    }
    
    public void delete(AuditLogRecord record)
    {
        getSession().delete(record);
    }
}
