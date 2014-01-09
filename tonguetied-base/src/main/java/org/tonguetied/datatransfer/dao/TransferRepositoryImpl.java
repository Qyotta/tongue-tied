/*
 * Copyright 2008 The Tongue-Tied Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.tonguetied.datatransfer.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.Translation;

/**
 * DAO facade to ORM. This facade allows access to permanent storage of keyword
 * related data via the Hibernate orm model.
 * 
 * @author bsion
 * 
 */
public class TransferRepositoryImpl extends HibernateDaoSupport implements TransferRepository {
    public void saveOrUpdate(Keyword keyword) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(keyword);
        getHibernateTemplate().flush();
    }

    public List<Translation> findTranslations(ExportParameters parameters) {
        if (logger.isDebugEnabled())
            logger.debug("attempting to find translations matching the criteria: " + parameters);
        if (CollectionUtils.isEmpty(parameters.getBundles())) {
            throw new IllegalArgumentException("bundles cannot be null or empty");
        }
        if (CollectionUtils.isEmpty(parameters.getCountries())) {
            throw new IllegalArgumentException("countries cannot be null or empty");
        }
        if (CollectionUtils.isEmpty(parameters.getLanguages())) {
            throw new IllegalArgumentException("languages cannot be null or empty");
        }
        if (CollectionUtils.isEmpty(parameters.getTranslationStates())) {
            throw new IllegalArgumentException("translation state cannot be null");
        }

        Query query = getSession().getNamedQuery(Translation.QUERY_FIND_TRANSLATIONS);
        query.setParameterList("countries", parameters.getCountries());
        query.setParameterList("bundles", parameters.getBundles());
        query.setParameterList("languages", parameters.getLanguages());
        query.setParameterList("states", parameters.getTranslationStates());
        return query.list();
    }
}
