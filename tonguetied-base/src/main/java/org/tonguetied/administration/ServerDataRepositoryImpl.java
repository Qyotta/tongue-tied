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

import static org.hibernate.criterion.Restrictions.idEq;
import static org.tonguetied.administration.ServerData.QUERY_GET_LATEST_SERVER_DATA;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * DAO facade to ORM. This facade allows access to permanent storage of 
 * ServerData related data via the Hibernate orm model.
 * 
 * @author bsion
 *
 */
public class ServerDataRepositoryImpl extends HibernateDaoSupport 
    implements ServerDataRepository
{

    public ServerData getLatestData()
    {
        
        ServerData serverData;
        try
        {
            Query query = getSession().getNamedQuery(QUERY_GET_LATEST_SERVER_DATA);
            query.setCacheable(true);
            serverData = (ServerData) query.uniqueResult();
        }
        catch (SQLGrammarException sge)
        {
            // if the table cannot be found in the database then return null
            DataAccessException dae = convertHibernateAccessException(sge);
            if (dae instanceof InvalidDataAccessResourceUsageException)
                serverData = null;
            else
                throw dae;
        }
        return serverData;
    }

    public ServerData getServerData(final Long id)
    {
        Criteria criteria = getSession().createCriteria(ServerData.class);
        criteria.add(idEq(id));
        criteria.setCacheable(true);
        return (ServerData) criteria.uniqueResult();
    }

    public void saveOrUpdate(ServerData serverData) throws DataAccessException
    {
        getHibernateTemplate().saveOrUpdate(serverData);
        getHibernateTemplate().flush();
    }

}
