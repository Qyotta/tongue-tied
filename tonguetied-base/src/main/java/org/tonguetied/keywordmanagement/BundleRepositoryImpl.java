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
package org.tonguetied.keywordmanagement;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.idEq;
import static org.tonguetied.keywordmanagement.Bundle.FIELD_NAME;
import static org.tonguetied.keywordmanagement.Bundle.FIELD_RESOURCE_NAME;
import static org.tonguetied.keywordmanagement.Bundle.QUERY_FIND_BUNDLES;
import static org.tonguetied.keywordmanagement.Bundle.QUERY_GET_BUNDLES;
import static org.tonguetied.keywordmanagement.Bundle.QUERY_GET_DEFAULT_BUNDLE;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * DAO facade to ORM. This facade allows access permanent storage of Bundle
 * related data via the Hibernate ORM model.
 * 
 * @author bsion
 * 
 */
public class BundleRepositoryImpl extends HibernateDaoSupport implements
        BundleRepository
{
    public Bundle getBundle(final Long id)
    {
        Criteria criteria = getSession().createCriteria(Bundle.class);
        criteria.add(idEq(id));
        criteria.setCacheable(true);
        return (Bundle) criteria.uniqueResult();
    }

    public Bundle getBundleByName(final String name)
    {
        Criteria criteria = getSession().createCriteria(Bundle.class);
        criteria.add(eq(FIELD_NAME, name));
        criteria.setCacheable(true);
        return (Bundle) criteria.uniqueResult();
    }

    public Bundle getBundleByResourceName(final String resourceName)
    {
        Criteria criteria = getSession().createCriteria(Bundle.class);
        criteria.add(eq(FIELD_RESOURCE_NAME, resourceName));
        criteria.setCacheable(true);
        return (Bundle) criteria.uniqueResult();
    }

    public Bundle getDefaultBundle()
    {
        Query query = getSession().getNamedQuery(QUERY_GET_DEFAULT_BUNDLE);
        query.setCacheable(true);
        return (Bundle) query.uniqueResult();
    }

    public List<Bundle> getBundles()
    {
        Query query = getSession().getNamedQuery(QUERY_GET_BUNDLES);
        query.setCacheable(true);
        return query.list();
    }

    public List<Bundle> findBundles(final String name, final String resourceName)
            throws IllegalArgumentException
    {
        Query query = getSession().getNamedQuery(QUERY_FIND_BUNDLES);
        query.setString(FIELD_NAME, name);
        query.setString(FIELD_RESOURCE_NAME, resourceName);
        
        return query.list();
    }

    public void saveOrUpdate(Bundle bundle) throws DataAccessException
    {
        if (bundle.isDefault())
        {
            Bundle defaultBundle = getDefaultBundle();
            if (defaultBundle != null
                    && !defaultBundle.getName().equals(bundle.getName()))
            {
                defaultBundle.setDefault(false);
                getHibernateTemplate().save(defaultBundle);
            }
        }
        
        if (bundle.getId() != null)
            getHibernateTemplate().merge(bundle);
        else
            getHibernateTemplate().saveOrUpdate(bundle);
        
        getHibernateTemplate().flush();
    }

    public void delete(Bundle bundle)
    {
        getSession().delete(bundle);
        getSession().flush();
    }
}
