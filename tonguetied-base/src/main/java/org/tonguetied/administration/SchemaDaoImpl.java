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

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.DialectFactory;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Concrete implementation of the {@link SchemaDao} interface, using jdbc as
 * the persistence mechanism.
 * 
 * @author bsion
 *
 */
public class SchemaDaoImpl extends JdbcDaoSupport implements SchemaDao
{
    public void createDatabase(final String[] schemas)
    {
        if (schemas == null || schemas.length <= 0)
            throw new IllegalArgumentException("database schema cannot be null");
        
        // execute all statements in the schema definition
        for (String schema : schemas)
        {
            for (String statement : schema.split(";"))
            {
                try
                {
                    if (logger.isDebugEnabled())
                        logger.debug("attempting to execute : " + statement);
                    
                    if (StringUtils.isNotBlank(statement))
                        getJdbcTemplate().execute(statement);
                }
                catch (BadSqlGrammarException dae)
                {
                    if (logger.isWarnEnabled())
                        logger.warn("The following statement failed to execute: " + statement);
                }
            }
            if (logger.isInfoEnabled())
                logger.info("create schema: " + schema);
        }
    }

    public String getSchemaFileName(final String dialectStr)
    {
        if (StringUtils.isBlank(dialectStr))
            throw new IllegalArgumentException("dialect cannot be null or empty");
        
        if (logger.isDebugEnabled())
            logger.debug("retrieving schema for dialect: " + dialectStr);
        
        String schemaFile = null;
        final Dialect dialect;
        try
        {
            dialect = DialectFactory.buildDialect(dialectStr);
        }
        catch (HibernateException he)
        {
            throw new RuntimeException(he);
        }
        
        if (dialect instanceof HSQLDialect)
        {
            schemaFile = "hsql-schema.sql";
        }
        else if (dialect instanceof MySQLDialect)
        {
            schemaFile = "mysql-schema.sql";
        }
        else if (dialect instanceof PostgreSQLDialect)
        {
            schemaFile = "postgresql-schema.sql";
        }
        else
        {
            logger.warn("dialect " + dialectStr + " is not supported");
        }
        
        return schemaFile;
    }
//  /**
//  * 
//  * @param dialectStr the string name of the SQL dialect
//  * @return
//  */
// private String getUpdateSchema(final String dialectStr, final String version)
// {
//     String schemaFile = null;
//     final Dialect dialect = DialectFactory.buildDialect(dialectStr);
//     if (dialect instanceof HSQLDialect)
//     {
//         schemaFile = DIR_SQL_UPDATE+"/"+version+"/hsql-update.sql";
//     }
//     else if (dialect instanceof MySQLDialect)
//     {
//         schemaFile = DIR_SQL_UPDATE+"/"+version+"/mysql-update.sql";
//     }
//     else if (dialect instanceof PostgreSQLDialect)
//     {
//         schemaFile = DIR_SQL_UPDATE+"/"+version+"/postgresql-update.sql";
//     }
//     
//     return schemaFile;
// }

}
