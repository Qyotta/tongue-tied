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
package org.tonguetied.web.servlet;

import static org.tonguetied.web.Constants.SHOW_ALL_KEYWORDS;
import static org.tonguetied.web.Constants.SHOW_ALL_USERS;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * Listener that initializes all the user session settings, when the user first
 * starts a {@link HttpSession} and tidies up any resources when the
 * {@link HttpSession} is lost.
 * 
 * @author bsion
 * 
 */
public class UserSessionInitializer implements HttpSessionListener
{
    private static final Logger logger = 
        Logger.getLogger(UserSessionInitializer.class);

    /**
     * Initialize user preferences when a user starts a new session. This method
     * will set up the tabbing flag.
     * 
     * @param event the notification event
     */
    public void sessionCreated(HttpSessionEvent event)
    {
        if (logger.isDebugEnabled())
            logger.debug("setting user session attributes");
        HttpSession session = event.getSession();
        session.setAttribute(SHOW_ALL_KEYWORDS, true);
        session.setAttribute(SHOW_ALL_USERS, true);
    }

    /**
     * Clean up user preferences on session destroy.
     * 
     * @param event the notification event
     */
    public void sessionDestroyed(HttpSessionEvent event)
    {
        if (logger.isDebugEnabled())
            logger.debug("removing user session attributes");
        HttpSession session = event.getSession();
        session.removeAttribute(SHOW_ALL_KEYWORDS);
        session.removeAttribute(SHOW_ALL_USERS);
    }
}
