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

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Parent;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.Language;

/**
 * This class represents an authority for the system that a {@link User} 
 * possesses.
 * 
 * @author bsion
 *
 */
@Embeddable
@AccessType("property")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class UserRight implements Comparable<UserRight>, Serializable {
    private Permission permission;
//    private Language language;
//    private Country country;
//    private Bundle bundle;
    private User user;
    
    private static final long serialVersionUID = -6950838004679280636L;

    /**
     * Create a new authority instance. Used by ORM 
     */
    public UserRight() {
    }

    /**
     * Create a new authority instance.
     * 
     * @param permission
     * @param language
     * @param country
     * @param bundle
     */
    public UserRight(Permission permission, Language language, Country country,
            Bundle bundle) {
        super();
        this.permission = permission;
//        this.language = language;
//        this.country = country;
//        this.bundle = bundle;
    }

    /**
     * @return the permission
     */
    @Column(nullable=false,length=13)
    @Enumerated(EnumType.STRING)
    public Permission getPermission() {
        return permission;
    }

    /**
     * @param permission the permission to set
     */
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

//    /**
//     * @return the language
//     */
//    @OneToOne(cascade=CascadeType.PERSIST)
//    @JoinColumn(name="LANGUAGE_ID")
//    public Language getLanguage() {
//        return language;
//    }
//
//    /**
//     * @param language the language to set
//     */
//    public void setLanguage(Language language) {
//        this.language = language;
//    }
//
//    /**
//     * @return the country
//     */
//    @OneToOne(cascade=CascadeType.PERSIST)
//    @JoinColumn(name="COUNTRY_ID")
//    public Country getCountry() {
//        return country;
//    }
//
//    /**
//     * @param country the country to set
//     */
//    public void setCountry(Country country) {
//        this.country = country;
//    }
//
//    /**
//     * @return the bundle
//     */
//    @OneToOne(cascade=CascadeType.PERSIST)
//    @JoinColumn(name="BUNDLE_ID")
//    public Bundle getBundle() {
//        return bundle;
//    }
//
//    /**
//     * @param bundle the bundle to set
//     */
//    public void setBundle(Bundle bundle) {
//        this.bundle = bundle;
//    }
    
    /**
     * @return the user
     */
    @Parent
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final UserRight other)
    {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(permission, other.permission);
        return builder.toComparison();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(27, 31);
        int hashCode = builder.append(permission).
//                    append(language).
//                    append(user).
//                    append(country).
//                    append(bundle).
                    toHashCode();
        
        return hashCode;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        // a good optimization
        if (this == obj)
            isEqual = true;
        else if (obj != null && getClass() == obj.getClass()) 
        {
            final UserRight other = (UserRight) obj;
            EqualsBuilder builder = new EqualsBuilder();
            isEqual = builder.append(permission, other.permission).
//                    append(user, other.user).
//                    append(language, other.language).
//                    append(country, other.country).
//                    append(bundle, other.bundle).
                    isEquals();  
        }
            
        return isEqual;
    }

    @Override
    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this, SHORT_PREFIX_STYLE);
        builder.append("permission", permission);
        return builder.toString();
    }
    
    public static enum Permission
    {
        ROLE_ADMIN, ROLE_USER, ROLE_VERIFIER, ROLE_DEV
        // should be read, write, verify, administer
    }
}
