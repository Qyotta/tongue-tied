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
import static org.tonguetied.keywordmanagement.Language.FIELD_CODE;
import static org.tonguetied.keywordmanagement.Language.QUERY_GET_LANAGUAGES;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.tonguetied.keywordmanagement.Language.LanguageCode;

/**
 * DAO facade to ORM. This facade allows access permanent storage of Language
 * related data via the Hibernate ORM model.
 * 
 * @author bsion
 * 
 */
public class LanguageRepositoryImpl extends HibernateDaoSupport implements
        LanguageRepository
{
    public List<Language> getLanguages()
    {
        Query query = getSession().getNamedQuery(QUERY_GET_LANAGUAGES);
        query.setCacheable(true);
        return query.list();
    }

    public Language getLanguage(final Long id)
    {
        Criteria criteria = getSession().createCriteria(Language.class);
        criteria.add(idEq(id));
        criteria.setCacheable(true);
        return (Language) criteria.uniqueResult();
    }

    public Language getLanguage(final LanguageCode code)
    {
        Criteria criteria = getSession().createCriteria(Language.class);
        criteria.add(eq(FIELD_CODE, code));
        criteria.setCacheable(true);
        return (Language) criteria.uniqueResult();
    }

    public void saveOrUpdate(Language language) throws DataAccessException
    {
        if (language.getId() != null)
            getHibernateTemplate().merge(language);
        else
            getHibernateTemplate().saveOrUpdate(language);
        getHibernateTemplate().flush();
    }

    public void delete(Language language)
    {
        getSession().delete(language);
        getSession().flush();
    }
}
