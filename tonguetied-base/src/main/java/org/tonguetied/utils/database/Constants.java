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
package org.tonguetied.utils.database;



/**
 * File containing embedded database constants.
 * 
 * @author bsion
 *
 */
public interface Constants
{
    /**
     * The key for the hibernate sql dialect.
     */
    public static final String KEY_HIBERNATE_DIALECT = "hibernate.dialect";
    /**
     * The key for the jdbc driver class name.
     */
    public static final String KEY_JDBC_DRIVER = "jdbc.driverClassName";
    /**
     * The key for the database url. This is the address of the database server
     * used by the jdbc connection.
     */
    public static final String KEY_JDBC_URL = "jdbc.url";
    /**
     * The key for the database port used by the jdbc connection.
     */
    public static final String KEY_JDBC_PORT = "jdbc.port";
    /**
     * The key for the name of the database used by the jdbc connection.
     */
    public static final String KEY_JDBC_NAME = "jdbc.name";
    /**
     * The key for the database user name used by the jdbc connection.
     */
    public static final String KEY_JDBC_USER_NAME = "jdbc.username";
    /**
     * The key for the database user's password used by the jdbc connection.
     */
    public static final String KEY_JDBC_PASSWORD = "jdbc.password";
    
    /**
     * The default database name. 
     */
    public static final String DB_NAME = "tonguetied";
}
