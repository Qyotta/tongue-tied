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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;


/**
 * Concrete implementation of the {@link UserDetailsService} that defers its
 * queried to the {@link UserRepository}.
 * 
 * @author bsion
 *
 */
public class UserDetailsServiceImpl implements UserDetailsService
{
    private UserRepository userRepository;
    private static final Logger logger = 
        Logger.getLogger(UserDetailsServiceImpl.class);

    /**
     * Find the {@link User} that matches the <code>username</code> by querying
     * the {@link UserRepository}.
     */
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException
    {
        if (logger.isDebugEnabled())
            logger.debug("searching for user with name = " + username);
        
        if (StringUtils.isBlank(username))
            throw new UsernameNotFoundException("unknown username " + username);
        
        final User user = userRepository.getUser(username);
        
        if (user == null)
            throw new UsernameNotFoundException("unknown username " + username);
        
        if (user.getUserRights() == null || user.getUserRights().isEmpty())
            throw new UsernameNotFoundException("User has no authorities");
        
        return user;
    }

    public void setUserRepository(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
}
