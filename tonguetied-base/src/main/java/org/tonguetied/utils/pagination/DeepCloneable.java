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
package org.tonguetied.utils.pagination;

/**
 * Interface used to mark an object as clonable, such that the cloned object is
 * a deep copy of the original.
 * 
 * @author bsion
 *
 */
public interface DeepCloneable<T> extends java.lang.Cloneable
{

    /**
     * Perform a deep copy of the implementing object.
     * 
     * @return a deep copy of the original object
     * @throws CloneNotSupportedException if the object T does not implement 
     * this interface.
     */
    public T deepClone() throws CloneNotSupportedException;
}
