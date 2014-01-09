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
package org.tonguetied.keywordmanagement;

/**
 * Exception indicating that the object trying to be deleted is referenced by 
 * other objects.
 * 
 * @author bsion
 *
 */
public class ReferenceException extends RuntimeException
{
    private static final long serialVersionUID = 7283310014281135567L;
    
    private String referencedObjectName;
    private int totalReferences;

    /**
     * Create a new instance of ReferenceException.
     * 
     * @param referencedObjectName the name referenced object
     * @param totalReferences the total number of references to the object
     */
    public ReferenceException(final String referencedObjectName, 
            final int totalReferences)
    {
        this.referencedObjectName = referencedObjectName;
        this.totalReferences = totalReferences;
    }

    /**
     * @return the name referenced object
     */
    public String getReferencedObjectName()
    {
        return referencedObjectName;
    }

    /**
     * @return the total number of references to the object
     */
    public int getTotalReferences()
    {
        return totalReferences;
    }
}
