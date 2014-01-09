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

/**
 * Interface to abstract away communication operations to the database and
 * database schema.
 * 
 * @author bsion
 *
 */
public interface SchemaDao
{
    /**
     * Connect to and create the database schema.
     * 
     * @param schema the sql statements used to create the DB schema for the
     * application
     * @throws IllegalArgumentException if the <code>schema</code> is null
     */
    void createDatabase(final String[] schema) throws IllegalArgumentException;
    
    /**
     * Evaluate the dialect string, and return the location of the appropriate 
     * db schema file.
     * 
     * @param dialectStr the string to evaluate
     * @return the db schema file name to use
     */
    String getSchemaFileName(final String dialectStr);
}
