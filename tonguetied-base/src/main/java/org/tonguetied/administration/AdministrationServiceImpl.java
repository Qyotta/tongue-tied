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

import org.springframework.dao.DataAccessException;

/**
 * Concrete implementation of the {@link AdministrationService} interface.
 * 
 * @author bsion
 *
 */
public class AdministrationServiceImpl implements AdministrationService
{
    private ServerDataRepository serverDataRepository;
    private SchemaDao schemaDao;

    public ServerData getLatestData()
    {
        return serverDataRepository.getLatestData();
    }

    public ServerData getServerData(final Long id)
    {
        return serverDataRepository.getServerData(id);
    }

    public void saveOrUpdate(ServerData serverData) throws DataAccessException
    {
        serverDataRepository.saveOrUpdate(serverData);
    }

    public void createDatabase(final String[] schema)
    {
        schemaDao.createDatabase(schema);
    }

    public String getSchemaFileName(String dialectStr)
    {
        return schemaDao.getSchemaFileName(dialectStr);
    }

    /**
     * Assign the schemaDao.
     *
     * @param schemaDao the schemaDao to set
     */
    public void setSchemaDao(final SchemaDao schemaDao)
    {
        this.schemaDao = schemaDao;
    }

    /**
     * Assign the serverDataRepository.
     *
     * @param serverDataRepository the serverDataRepository to set
     */
    public void setServerDataRepository(final ServerDataRepository serverDataRepository)
    {
        this.serverDataRepository = serverDataRepository;
    }
}
