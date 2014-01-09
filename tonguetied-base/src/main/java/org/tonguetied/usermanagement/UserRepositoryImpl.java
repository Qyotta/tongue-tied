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
package org.tonguetied.usermanagement;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.idEq;
import static org.tonguetied.usermanagement.User.FIELD_EMAIL;
import static org.tonguetied.usermanagement.User.FIELD_FIRSTNAME;
import static org.tonguetied.usermanagement.User.FIELD_LASTNAME;
import static org.tonguetied.usermanagement.User.FIELD_USERNAME;
import static org.tonguetied.usermanagement.User.QUERY_GET_USERS;
import static org.tonguetied.usermanagement.User.QUERY_USER_COUNT;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.tonguetied.utils.pagination.PaginatedList;

/**
 * DAO facade to ORM. This facade allows access to permanent storage of User
 * related data via the Hibernate orm model.
 * 
 * @author bsion
 * 
 */
public class UserRepositoryImpl extends HibernateDaoSupport implements
        UserRepository
{
    public User getUser(final String username)
    {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(eq(FIELD_USERNAME, username));
        return (User) criteria.uniqueResult();
    }

    public User getUser(final Long id)
    {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.add(idEq(id));
        return (User) criteria.uniqueResult();
    }

    public PaginatedList<User> getUsers(final Integer firstResult,
            final Integer maxResults)
    {
        Query query = getSession().getNamedQuery(QUERY_GET_USERS);
        query.setCacheable(true);
        if (firstResult != null) query.setFirstResult(firstResult);
        if (maxResults != null) query.setMaxResults(maxResults);
        
        Long maxListSize = 0L;
        final List<User> queryList = query.list();
        if (queryList.size() > 0)
            maxListSize = (Long) getSession().getNamedQuery(
                    QUERY_USER_COUNT).uniqueResult();
        
        return new PaginatedList<User>(queryList, maxListSize.intValue());
    }

    public PaginatedList<User> findUsers(final User user, 
            final Integer firstResult,
            final Integer maxResults)
    {
        Criteria criteria = createCriteria(user);
        criteria.addOrder(Order.asc(FIELD_USERNAME));
        if (firstResult != null) criteria.setFirstResult(firstResult);
        if (maxResults != null) criteria.setMaxResults(maxResults);
        
        int maxListSize = 0;
        final List<User> criteriaList = criteria.list();
        if (criteriaList.size() > 0)
        {
            Criteria criteria2 = createCriteria(user);
            criteria2.setProjection(Projections.rowCount());
            
            maxListSize = (Integer) criteria2.uniqueResult();
        }

        return new PaginatedList<User>(criteria.list(), maxListSize);
    }

    /**
     * Create a search criteria object.
     * 
     * @param user the object used to create the search criteria
     * @return the criteria object used to find matches
     */
    private Criteria createCriteria(final User user)
    {
        Criteria criteria = getSession().createCriteria(User.class);
        criteria.setCacheable(true);
        addCriteria(criteria, FIELD_USERNAME, user.getUsername());
        addCriteria(criteria, FIELD_FIRSTNAME, user.getFirstName());
        addCriteria(criteria, FIELD_LASTNAME, user.getLastName());
        addCriteria(criteria, FIELD_EMAIL, user.getEmail());
        return criteria;
    }

    /**
     * Add a restriction to the search criteria.
     * 
     * @param criteria the criteria object to amend
     * @param fieldName the name of the field on which to apply the restriction
     * @param value the value of the field restriction
     */
    private void addCriteria(Criteria criteria, final String fieldName, final String value)
    {
        if (StringUtils.isNotEmpty(value))
            criteria.add(Restrictions.ilike(fieldName, value, MatchMode.ANYWHERE));
    }

    public void saveOrUpdate(User user) throws DataAccessException
    {
        getHibernateTemplate().saveOrUpdate(user);
        getHibernateTemplate().flush();
    }

    public void delete(User user)
    {
        getSession().delete(user);
    }
}
