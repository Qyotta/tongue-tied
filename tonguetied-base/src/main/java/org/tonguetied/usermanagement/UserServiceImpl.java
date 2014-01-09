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

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.tonguetied.utils.pagination.PaginatedList;


/**
 * Concrete implementation of the {@link UserService} interface.
 * 
 * @author bsion
 *
 */
public class UserServiceImpl implements UserService
{
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    
    private static final Logger logger = 
        Logger.getLogger(UserServiceImpl.class);

    public User getUser(final String username)
    {
        return userRepository.getUser(username);
    }

    public User getUser(final Long id)
    {
        return userRepository.getUser(id);
    }

    public List<User> getUsers()
    {
        return getUsers(0, null);
    }

    public PaginatedList<User> getUsers(final Integer firstResult,
            final Integer maxResults)
    {
        return userRepository.getUsers(firstResult, maxResults);
    }

    public PaginatedList<User> findUsers(final User user, final Integer firstResult,
            final Integer maxResults)
    {
        return userRepository.findUsers(user, firstResult, maxResults);
    }

    /**
     * {@inheritDoc}
     * When the user is saved then a unidirectional encryption of the password 
     * is made. This involves making a hash of the password.
     */
    public void saveOrUpdate(User user)
    {
        saveOrUpdate(user, (user.getId() == null));
    }

    /**
     * {@inheritDoc}
     * When the user is saved then a unidirectional encryption of the password 
     * is made. This involves making a hash of the password.
     */
    public void saveOrUpdate(User user, boolean encodePassword)
    {
        if (logger.isDebugEnabled()) logger.debug("persisting user: " + user);
        if (encodePassword)
        {
            encodePassword(user, user.getPassword());
        }
        userRepository.saveOrUpdate(user);
    }

    /**
     * Perform a unidirectional encryption of the password. This involves 
     * making a hash of the password. The {@link #passwordEncoder} determines 
     * the encryption mechanism.
     * @param newPassword the raw value of the new password
     * 
     * @see #setPasswordEncoder(PasswordEncoder)
     */
    protected void encodePassword(User user, final String newPassword)
    {
        if (logger.isDebugEnabled()) logger.debug("endcoding user password");
        final String encoded = 
            passwordEncoder.encodePassword(newPassword, user.getUsername());
        
        user.setPassword(encoded);
    }
    /**
     * {@inheritDoc}
     */
    public void changePassword(User user, final String oldPassword, final String newPassword)
            throws AuthenticationException 
    {
        if (!isPasswordValid(user, oldPassword))
            throw new AuthenticationException("invalid password");
        if (newPassword == null)
            throw new IllegalArgumentException("password cannot be null");
        
        if (logger.isDebugEnabled()) logger.debug("changing user password");
        encodePassword(user, newPassword);
        
        userRepository.saveOrUpdate(user);
    }

    /**
     * Determine if the password supplied is valid.
     * 
     * @param user the user to validate against
     * @param password the value to validate
     * @return <code>true</code> if the password matches the users password,
     * <code>false</code> otherwise.
     * @throws DataAccessException
     */
    private boolean isPasswordValid(final User user, final String password)
            throws DataAccessException
    {
        return passwordEncoder.isPasswordValid(
                user.getPassword(), password, user.getUsername());
    }

    /**
     * 
     * @param userRepository the DAO used interact with the storage mechanism 
     * for user data
     */
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param passwordEncoder the encoder used to encrypt the user's password
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
