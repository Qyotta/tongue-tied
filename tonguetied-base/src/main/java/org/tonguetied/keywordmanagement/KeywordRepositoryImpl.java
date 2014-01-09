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

import static org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY;
import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Restrictions.conjunction;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.idEq;
import static org.tonguetied.keywordmanagement.Keyword.FIELD_ID;
import static org.tonguetied.keywordmanagement.Keyword.FIELD_KEYWORD;
import static org.tonguetied.keywordmanagement.Keyword.FIELD_TRANSLATIONS;
import static org.tonguetied.keywordmanagement.Keyword.QUERY_GET_KEYWORDS;
import static org.tonguetied.keywordmanagement.Keyword.QUERY_GET_KEYWORDS_DESC;
import static org.tonguetied.keywordmanagement.Keyword.QUERY_KEYWORD_COUNT;

import java.util.List;
import java.util.SortedSet;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.tonguetied.utils.pagination.Order;
import org.tonguetied.utils.pagination.PaginatedList;

/**
 * DAO facade to ORM. This facade allows access permanent storage of Keyword
 * related data via the Hibernate orm model.
 * 
 * @author bsion
 * 
 */
public class KeywordRepositoryImpl extends HibernateDaoSupport implements
        KeywordRepository
{
    public Keyword getKeyword(final Long id)
    {
        Criteria criteria = getSession().createCriteria(Keyword.class);
        criteria.add(idEq(id));
        return (Keyword) criteria.uniqueResult();
    }

    public Keyword getKeyword(final String keywordString)
    {
        Criteria criteria = getSession().createCriteria(Keyword.class);
        criteria.add(eq(FIELD_KEYWORD, keywordString));
        return (Keyword) criteria.uniqueResult();
    }

    public PaginatedList<Keyword> getKeywords(final Integer firstResult,
            final Integer maxResults, final Order order)
    {
        final String queryName;
        if (Order.desc == order)
            queryName = QUERY_GET_KEYWORDS_DESC;
        else
            queryName = QUERY_GET_KEYWORDS;
        Query query = getSession().getNamedQuery(queryName);
        if (firstResult != null) query.setFirstResult(firstResult);
        if (maxResults != null) query.setMaxResults(maxResults);
        
        Long maxListSize = 0L;
        final List<Keyword> queryList = query.list();
        if (queryList.size() > 0)
            maxListSize = (Long) getSession().getNamedQuery(
                    QUERY_KEYWORD_COUNT).uniqueResult();
        
        return new PaginatedList<Keyword>(queryList, maxListSize.intValue());
    }

    public PaginatedList<Keyword> findKeywords(Keyword keyword,
                                            final boolean ignoreCase,
                                            final Order order,
                                            final Integer firstResult, 
                                            final Integer maxResults) 
            throws IllegalArgumentException
    {
        if (keyword == null)
        {
            throw new IllegalArgumentException("keyword cannot be null");
        }
        final MatchMode matchMode = MatchMode.ANYWHERE;
        Example criterionKeyword = Example.create(keyword);
        criterionKeyword.enableLike(matchMode);
        if (ignoreCase)
        {
            criterionKeyword.ignoreCase();
        }

        // Normally, Hibernate performs a left-outer join, when searching for 
        // an object with collections using Criteria. This returns a ResultSet
        // that contains duplicate objects. In order to get a unique list of 
        // Keywords with paginated support, we need to a nested query to find
        // distinct matching ids, then get the Keywords. The end result is a
        // subselect in the main query, but only one query is sent.
        DetachedCriteria dc = DetachedCriteria.forClass(Keyword.class);
        dc.add(criterionKeyword);
        dc.setResultTransformer(DISTINCT_ROOT_ENTITY);
        
        Conjunction conjunction = createTranslationConditions(
                keyword.getTranslations(), ignoreCase, matchMode);
        if (conjunction != null)
            dc.createCriteria(FIELD_TRANSLATIONS).add(conjunction);
        dc.setProjection(Projections.id());

        Criteria criteria = getSession().createCriteria(Keyword.class);
        criteria.add(Subqueries.propertyIn(FIELD_ID, dc));
        if (Order.desc == order)
            criteria.addOrder(desc(FIELD_KEYWORD));
        else
            criteria.addOrder(asc(FIELD_KEYWORD));
        if (firstResult != null) criteria.setFirstResult(firstResult);
        if (maxResults != null) criteria.setMaxResults(maxResults);
        
        final List<Keyword> criteriaList = criteria.list();
        int maxListSize = 0;
        if (criteriaList.size() > 0)
        {
            maxListSize = calculateMaxListSize(criterionKeyword, conjunction);
        }
        
        return new PaginatedList<Keyword>(criteriaList, maxListSize);
    }

    /**
     * Run a query to calculate the total number of records that match the 
     * search criteria.
     * 
     * @param criterion used to create the search criteria
     * @param conjunction used to create the search criteria
     * @return the total number of matches for the search criteria
     */
    private int calculateMaxListSize(Example criterion, Conjunction conjunction)
    {
        Criteria criteria = getSession().createCriteria(Keyword.class);
        criteria.add(criterion);
        if (conjunction != null)
            criteria.createCriteria(FIELD_TRANSLATIONS).add(conjunction);
        criteria.setProjection(Projections.countDistinct(FIELD_ID));
        
        return (Integer) criteria.uniqueResult();
    }

    /**
     * Create the search criteria for a {@link Translation}.
     * 
     * @param translations the translation to add to the criteria
     * @param ignoreCase flag indicating if case should be ignored during search
     * @param matchMode flag indicating the type of string pattern matching
     * @return the additional search parameters for the {@link Translation}
     * fields
     */
    private Conjunction createTranslationConditions(
            SortedSet<Translation> translations,
            final boolean ignoreCase,
            final MatchMode matchMode)
    {
        Conjunction conjunction = null;
        if (translations != null && !translations.isEmpty())
        {
            final Translation translation = translations.first();

            Example criterionTranslation = Example.create(translation);
            criterionTranslation.enableLike(matchMode);
            if (ignoreCase)
            {
                criterionTranslation.ignoreCase();
            }

            conjunction = conjunction();
            conjunction.add(criterionTranslation);

            addBundleCriteria(conjunction, translation.getBundle());
            addCountryCriteria(conjunction, translation.getCountry());
            addLanguageCriteria(conjunction, translation.getLanguage());
        }
        
        return conjunction;
    }

    /**
     * Adds an equality comparison for a {@link Bundle}.
     * 
     * @param junction
     * @param bundle the bundle to compare
     */
    private void addBundleCriteria(Junction junction, final Bundle bundle)
    {
        if (bundle != null)
        {
            junction.add(eq("bundle", bundle));
        }
    }

    /**
     * Adds an equality comparison for a {@link Country}.
     * 
     * @param junction
     * @param country the country to compare
     */
    private void addCountryCriteria(Junction junction, final Country country)
    {
        if (country != null)
        {
            junction.add(eq("country", country));
        }
    }

    /**
     * Adds an equality comparison for a {@link Language}.
     * 
     * @param junction
     * @param language the language to compare
     */
    private void addLanguageCriteria(Junction junction, final Language language)
    {
        if (language != null)
        {
            junction.add(eq("language", language));
        }
    }

    public void saveOrUpdate(Keyword keyword) throws DataAccessException
    {
        if (keyword.getId() == null)
            getHibernateTemplate().saveOrUpdate(keyword);
        else
            getHibernateTemplate().merge(keyword);
        getHibernateTemplate().flush();
    }

    public int getReferences(final String propertyName, final Object value)
    {
        Criteria criteria = getSession().createCriteria(Translation.class);
        criteria.add(Restrictions.eq(propertyName, value));
        criteria.setProjection(Projections.rowCount());
        return ((Integer) criteria.uniqueResult());
    }

    public void delete(Keyword keyword)
    {
        // remove any previously loaded entities before deleting the keyword
        getSession().clear();
        getSession().delete(keyword);
    }
}
