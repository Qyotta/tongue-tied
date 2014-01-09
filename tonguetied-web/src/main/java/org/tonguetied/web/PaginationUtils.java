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
package org.tonguetied.web;

import static org.displaytag.tags.TableTagParameters.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.displaytag.util.ParamEncoder;
import org.tonguetied.utils.pagination.Order;

/**
 * Utility class of common methods for paginated display.
 * 
 * @author bsion
 *
 */
public final class PaginationUtils
{
    private static final Logger logger = 
        Logger.getLogger(PaginationUtils.class);

    /**
     * Determine the value for the first result. The value is calculated as:
     * (selectedPage - 1) * {@link PreferenceForm#getMaxResults()}
     * 
     * If the start point cannot be calculated then a value of 0 is returned.
     * 
     * @param tableId the table tag id of the table
     * @param maxResults the size of the results
     * @param request the HTTP request 
     * @return the value to be used as the first result of a paginated query.
     */
    static int calculateFirstResult(final String tableId, final int maxResults, 
            HttpServletRequest request)
    {
        final String pageParam = 
            new ParamEncoder(tableId).encodeParameterName(PARAMETER_PAGE);

        Integer selectedPage = RequestUtils.getIntegerParameter(request, pageParam);
        if (selectedPage == null)
        {
            final Object attr = request.getSession().getAttribute(pageParam);
            if (attr != null)
                selectedPage = (Integer) attr;
            if (selectedPage == null)
                selectedPage = 0;
        }
        
        int firstResult = 0;
        if (selectedPage > 0)
        {
            if (logger.isDebugEnabled())
                logger.debug("selected page = " + selectedPage);
            firstResult = (selectedPage-1) * maxResults;
        }
        
        return firstResult;
    }
    
    /**
     * Get the Column and and sort order selected by the user.
     * 
     * @param tableId the table tag id of the table
     * @param request the HTTP request 
     * @return key value pair of column and sort order
     */
    static KeyValue<String, Order> getOrder(final String tableId, HttpServletRequest request)
    {
        final String sortParam = 
            new ParamEncoder(tableId).encodeParameterName(PARAMETER_SORT);
        final String sort = request.getParameter(sortParam);
        if (logger.isDebugEnabled())
            logger.debug("selected sort = " + sort);
        KeyValue<String, Order> keyValue = null;
        if (sort != null)
        {
            final String orderParam = 
                new ParamEncoder(tableId).encodeParameterName(PARAMETER_ORDER);
            Integer order = 0;
            try
            {
                order = RequestUtils.getIntegerParameter(request, orderParam);
                if (logger.isDebugEnabled())
                    logger.debug("order = " + order);
                if (order > 0 && order <= Order.values().length)
                    order--;
                else
                    order = 0;
            }
            catch (NumberFormatException nfe)
            {
                logger.warn(nfe);
            }
            keyValue = new KeyValue<String, Order>(sort, Order.values()[order]);
        }
        
        return keyValue;
    }
    
    /**
     * Remove the page parameter for a table from the session.
     * 
     * @param tableId the table tag id of the table
     * @param request the HTTP request 
     */
    static void remove(final String tableId, HttpServletRequest request)
    {
        final String pageParam = 
            new ParamEncoder(tableId).encodeParameterName(PARAMETER_PAGE);
        if (logger.isDebugEnabled())
            logger.debug("removing attribute " + pageParam + "from session");

        request.getSession().removeAttribute(pageParam);
    }
    
    /**
     * A representation of a key value pair.
     * 
     * @author bsion
     *
     * @param <K>
     * @param <V>
     */
    public static class KeyValue<K, V>
    {
        private K key;
        private V value;
        
        /**
         * Create a new instance of KeyValue.
         *
         * @param key
         * @param value
         */
        private KeyValue(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        /**
         * @return the key
         */
        public K getKey()
        {
            return key;
        }

        /**
         * @return the value
         */
        public V getValue()
        {
            return value;
        }
    }
}
