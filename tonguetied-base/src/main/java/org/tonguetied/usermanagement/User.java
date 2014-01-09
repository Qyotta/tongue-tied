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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;

/**
 * A built in user of the system.
 * 
 * @author bsion
 * 
 */
@Entity
@AccessType("property")
@NamedQueries({
    @NamedQuery(name=User.QUERY_USER_COUNT,query="select count(*) from User"),
    @NamedQuery(name=User.QUERY_GET_USERS, query = "from User u order by u.username")
})
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name=User.TABLE_USER)
public class User implements UserDetails
{
    private Long id;
    private String username;
    transient private String password;
    transient private String repeatedPassword = null;
    private String firstName;
    private String lastName;
    private boolean isEnabled;
    private String email;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private SortedSet<UserRight> userRights;
    private GrantedAuthority[] grantedAuthorities;

    // This attribute is used for optimistic concurrency control in DB
    private Integer version;

    private static final String FIELD_ID = "id";
    public static final String FIELD_LASTNAME = "lastName";
    public static final String FIELD_FIRSTNAME = "firstName";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_REPEATED_PASSWORD = "repeatedPassword";

    public static final String TABLE_USER = "internal_user";
    public static final String TABLE_AUTHORITIES = "authorities";
    private static final String COL_ID = TABLE_USER + "_id";
    private static final String FK_AUTHORITIES = 
        "fk_"+ TABLE_USER + "_" + TABLE_AUTHORITIES;
    protected static final String QUERY_GET_USERS = "get.users";
    protected static final String QUERY_USER_COUNT = "user.count";

    private static final long serialVersionUID = -7800860686467033859L;

    /**
     * Create a new instance of the User object.
     */
    public User()
    {
        this.userRights = new TreeSet<UserRight>();
    }

    /**
     * Create a new instance of the User object.
     * 
     * @param username unique name of the user
     * @param password user's password
     * @param firstName the user's given name
     * @param lastName the surname of the user
     * @param email the email address of the user
     * @param isEnabled flag indicating if the user is active
     * @param isAccountNonExpired flag indicating whether the user's account has
     *        expired
     * @param isAccountNonLocked flag indicating whether the user is locked or
     *        unlocked
     * @param isCredentialsNonExpired flag indicating whether the user's
     *        credentials (password) has expired
     */
    public User(final String username, final String password,
            final String firstName, final String lastName, final String email,
            final boolean isEnabled, final boolean isAccountNonExpired,
            final boolean isAccountNonLocked,
            final boolean isCredentialsNonExpired)
    {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEnabled = isEnabled;
        this.email = email;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.userRights = new TreeSet<UserRight>();
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator="user_generator")
    @SequenceGenerator(name="user_generator",sequenceName="internal_user_id_seq")
    @Column(name=COL_ID)
    public Long getId()
    {
        return id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    /**
     * @return the first name of the User
     */
    @Column(name="first_name",nullable=false)
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @param firstName the first name of the User to set
     */
    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    /**
     * @return the last name of the User
     */
    @Column(name="last_name",nullable=false)
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @param lastName the last name of the User to set
     */
    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    /**
     * @return the password of the User
     */
    @Column(nullable=false)
    public String getPassword()
    {
        return password;
    }

    /**
     * Used as a dto method.
     * 
     * @param password the password to set for the User
     */
    public void setPassword(final String password)
    {
        this.password = password;
    }

    /**
     * @return the repeatedPassword
     */
    @Transient
    public String getRepeatedPassword()
    {
        return repeatedPassword;
    }

    /**
     * @param repeatedPassword the repeatedPassword to set
     */
    public void setRepeatedPassword(final String repeatedPassword)
    {
        this.repeatedPassword = repeatedPassword;
    }

    /**
     * @return the username of the User
     */
    @Column(unique=true,nullable=false)
    public String getUsername()
    {
        return username;
    }

    /**
     * @param username the username of the User to set
     */
    public void setUsername(final String username)
    {
        this.username = username;
    }

    /**
     * This field is used for user authentication
     * 
     * @return an indicator specifying if the user is active or not.
     */
    @Column(nullable=false)
    public boolean isEnabled()
    {
        return isEnabled;
    }

    /**
     * @param isEnabled the active flag to set
     */
    public void setEnabled(final boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }

    /**
     * @return the email address of this User
     */
    @Column(nullable=false)
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email the email address to set
     */
    public void setEmail(final String email)
    {
        this.email = email;
    }

    /**
     * @return the set of authorized permissions granted to a User
     */
    @CollectionOfElements(fetch=FetchType.LAZY)
    @Sort(type=SortType.NATURAL)
    @JoinTable(name=TABLE_AUTHORITIES,joinColumns=@JoinColumn(name="user_id"))
    @Cascade(CascadeType.ALL)
    @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
    @GeneratedValue(strategy=GenerationType.AUTO,generator="authorities_generator")
    @SequenceGenerator(name="authorities_generator",sequenceName="authorities_user_id_seq")
    @ForeignKey(name=FK_AUTHORITIES)
    public SortedSet<UserRight> getUserRights()
    {
        return userRights;
    }

    /**
     * @param userRights the authorized permissions to set
     */
    public void setUserRights(SortedSet<UserRight> userRights)
    {
        this.userRights = userRights;
        if (userRights != null && !userRights.isEmpty())
        {
            Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            GrantedAuthority authority;
            for (UserRight userRight : getUserRights())
            {
                authority = new GrantedAuthorityImpl(userRight.getPermission()
                        .name());
                authorities.add(authority);
            }

            this.setAuthorities(authorities.toArray(
                    new GrantedAuthority[authorities.size()]));
        }
    }

    /**
     * Convenience method to add a permission to this User.
     * 
     * @param userRight the authorized permission to add
     * @throws IllegalArgumentException if the <tt>userRight</tt> is
     *         <tt>null</tt>
     */
    public void addUserRight(UserRight userRight)
    {
        if (userRight == null)
        {
            throw new IllegalArgumentException("userRight cannot be null");
        }

        this.userRights.add(userRight);
        final GrantedAuthority authority = new GrantedAuthorityImpl(userRight
                .getPermission().name());
        Set<GrantedAuthority> authorities = getAuthoritiesAsSet();
        if (!authorities.contains(authority))
        {
            authorities.add(authority);
        }
        this.setAuthorities(authorities.toArray(
                new GrantedAuthority[authorities.size()]));
    }

    /**
     * Convenience method to remove a {@link UserRight} from this User. If the
     * UserRight does not exist then no action is taken.
     * 
     * @param userRight the authorized permission to remove
     * @throws IllegalArgumentException if the <tt>userRight</tt> is
     *         <tt>null</tt> or the <tt>permission</tt> is <tt>null</tt>
     */
    public void removeUserRight(UserRight userRight)
    {
        if (userRight == null || userRight.getPermission() == null)
        {
            throw new IllegalArgumentException("userRight cannot be null");
        }

        this.userRights.remove(userRight);
        
        final GrantedAuthority authority = 
            new GrantedAuthorityImpl(userRight.getPermission().name());
        Set<GrantedAuthority> authorities = getAuthoritiesAsSet();
        authorities.remove(authority);
        this.setAuthorities(authorities.toArray(
                new GrantedAuthority[authorities.size()]));
    }

    /**
     * Convert the authorities array to a set. If the authorities array is null
     * then an empty set is returned.
     * 
     * @return a set of authorities.
     */
    @Transient
    private Set<GrantedAuthority> getAuthoritiesAsSet()
    {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        if (this.grantedAuthorities != null)
        {
            authorities = new HashSet<GrantedAuthority>(Arrays
                    .asList(this.grantedAuthorities));
        }
        return authorities;
    }

    @Column(name="account_non_expired",nullable=false)
    public boolean isAccountNonExpired()
    {
        return isAccountNonExpired;
    }

    /**
     * @param isAccountNonExpired the flag indicating if the account has expired
     */
    public void setAccountNonExpired(final boolean isAccountNonExpired)
    {
        this.isAccountNonExpired = isAccountNonExpired;
    }

    @Column(name="account_non_locked",nullable=false)
    public boolean isAccountNonLocked()
    {
        return isAccountNonLocked;
    }

    /**
     * @param isAccountNonLocked the isAccountNonLocked to set
     */
    public void setAccountNonLocked(final boolean isAccountNonLocked)
    {
        this.isAccountNonLocked = isAccountNonLocked;
    }

    @Column(name="credentials_non_expired",nullable=false)
    public boolean isCredentialsNonExpired()
    {
        return isCredentialsNonExpired;
    }

    /**
     * @param isCredentialsNonExpired the isCredentialsNonExpired to set
     */
    public void setCredentialsNonExpired(final boolean isCredentialsNonExpired)
    {
        this.isCredentialsNonExpired = isCredentialsNonExpired;
    }

    /**
     * Returns a cloned array of {@link GrantedAuthority}s. This is done for
     * security reasons, as the authorities are mutable.
     * 
     * @return the {@link GrantedAuthority}s for this user.
     */
    @Transient
    public GrantedAuthority[] getAuthorities()
    {
        return grantedAuthorities == null ? null : grantedAuthorities.clone();
    }

    public void setAuthorities(GrantedAuthority[] authorities)
    {
        // Assert.notNull(authorities, "Cannot pass a null GrantedAuthority
        // array");
        // for (int i = 0; i < authorities.length; i++) {
        // Assert.notNull(authorities[i], "Granted authority element " + i + "
        // is null - GrantedAuthority[] cannot contain any null elements");
        // }
        this.grantedAuthorities = authorities.clone();
    }

    /**
     * This field is used for optimistic locking.
     * 
     * @return the version
     */
    @Version
    @Column(name="optlock")
    public Integer getVersion()
    {
        return version;
    }

    /**
     * This field is used for optimistic locking.
     * 
     * @param version the version to set
     */
    public void setVersion(final Integer version)
    {
        this.version = version;
    }

    /**
     * Clears the contents of this object, sets all attributes to their initial
     * value state. 
     */
    public void initialize()
    {
        this.username = null;
        this.lastName = null;
        this.firstName = null;
        this.email = null;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean isEqual = false;
        // a good optimization
        if (this == obj)
            isEqual = true;
        else if (obj != null && getClass() == obj.getClass())
        {
            final User other = (User) obj;
            EqualsBuilder builder = new EqualsBuilder();
            isEqual = builder.append(username, other.username).append(
                    firstName, other.firstName)
                    .append(lastName, other.lastName).append(isEnabled,
                            other.isEnabled).append(email, other.email).append(
                            isAccountNonExpired, other.isAccountNonExpired)
                    .append(isAccountNonLocked, other.isAccountNonLocked)
                    .append(isCredentialsNonExpired,
                            other.isCredentialsNonExpired).
                    // append(userRights, other.userRights).
                    isEquals();
        }

        return isEqual;
    }

    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(17, 19);
        int hashCode = builder.append(username).append(lastName).append(
                firstName).append(isEnabled).append(email).append(
                isAccountNonExpired).append(isAccountNonLocked).append(
                isCredentialsNonExpired).
        // append(userRights).
                toHashCode();

        return hashCode;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(FIELD_ID, id).append(FIELD_USERNAME, username).append(
                        "password", "********")
                .append(FIELD_LASTNAME, lastName).append(FIELD_FIRSTNAME,
                        firstName).append(FIELD_EMAIL, email).append(
                        "isEnabled", isEnabled).append("isAccountNonExpired",
                        isAccountNonExpired).append("isAccountNonLocked",
                        isAccountNonLocked).append("isCredentialsNonExpired",
                        isCredentialsNonExpired).append("userRights",
                        userRights).toString();
    }
}
