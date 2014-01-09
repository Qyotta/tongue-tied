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
 * Interface defining ServerData DAO facade for TongueTied storage.
 * 
 * @author bsion
 *
 */
public interface ServerDataRepository
{
    /**
     * Retrieve the system by the unique identifier from permanent storage.
     * 
     * @param id the unique key to search for
     * @return the {@link ServerData} matching the <code>id</code> or 
     * <code>null</code> if no match is found
     */
    ServerData getServerData(final Long id);
    
    /**
     * Retrieve the most recent {@link ServerData} entry.
     * 
     * @return the most recent {@link ServerData} entry or <code>null</code> if no
     * match is found
     */
    ServerData getLatestData();
    
    /**
     * Persist a {@link ServerData} object to permanent storage.
     * 
     * @param serverData the system details to save or update.
     * @throws DataAccessException if the operation fails.
     */
    void saveOrUpdate(ServerData serverData) throws DataAccessException;
}
