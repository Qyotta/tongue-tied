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

/**
 * Exception indicating that the user cannot be authenticated.
 * 
 * @author bsion
 *
 */
public class AuthenticationException extends RuntimeException 
{
    private static final long serialVersionUID = 8649831549679257594L;

    /**
     * Create a new instance of AuthenticationException.
     * 
     * @param message the message of the error
     */
    public AuthenticationException(String message) {
        super(message);
    }

}
