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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.tonguetied.audit.AuditSupport;

/**
 * Class describing the country as it relates to resource management. The 
 * country is one aspect used to determine a {@link Translation}.
 * 
 * @author bsion
 */
@Entity
@AccessType("field")
@NamedQuery(name=Country.QUERY_GET_COUNTRIES,query="from Country c order by c.name")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Table(name=Country.TABLE_COUNTRY)
public class Country implements Comparable<Country>, AuditSupport
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO,generator="country_generator")
    @SequenceGenerator(name="country_generator",sequenceName="country_id_seq")
    @Column(name=COL_ID)
    private Long id;
    @Column(unique=true,nullable=false,length=7)
    @Enumerated(EnumType.STRING)
    private CountryCode code;
    @Column(nullable=false)
    private String name;
    
    public static final String FIELD_CODE = "code";
    public static final String FIELD_NAME = "name";
    public static final String TABLE_COUNTRY = "country";
    private static final String COL_ID = TABLE_COUNTRY + "_id";
    protected static final String QUERY_GET_COUNTRIES = "get.countries";
    
    public CountryCode getCode()
    {
        return code;
    }
    
    public void setCode(final CountryCode code)
    {
        this.code = code;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(final Long id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    
    public void setName(final String name)
    {
        this.name = name;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final Country other) {
        return new CompareToBuilder().append(code, other.code).toComparison();
    }
    
    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        // a good optimization
        if (this == obj)
        {
            isEqual = true;
        }
        else if (obj instanceof Country)
        {
            final Country other = (Country)obj;
            
            isEqual = code.equals(other.code)
                    && name.equals(other.name);
        }
        
        return isEqual;
    }
    
    @Override
    public int hashCode()
    {
        HashCodeBuilder builder = new HashCodeBuilder(15, 29);
        builder.append(this.code).append(this.name);

        return builder.toHashCode();
    }
    
    public String toLogString()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return new ReflectionToStringBuilder(this, 
                ToStringStyle.SHORT_PREFIX_STYLE).toString();
    }
    
    /**
     * This enum contains the list of country codes according to iso3166
     * arranged in alphabetical order of countries' names in English
     *
     * @see <a href="http://www.iso.org/iso/en/prods-services/iso3166ma/02iso-3166-code-lists/list-en1.html">ISO Country codes</a>
     */
    public enum CountryCode {
        /**
         * Non ISO code representing the default language code
         */
        DEFAULT,
        AF,AX,AL,DZ,AS,AD,AO,AI,AQ,AG,AR,AM,AW,AU,AT,AZ,BS,BH,BD,BB,BY,BE,BZ,BJ,
        BM,BT,BO,BA,BW,BV,BR,IO,BN,BG,BF,BI,KH,CM,CA,CV,KY,CF,TD,CL,CN,CX,CC,CO,
        KM,CG,CD,CK,CR,CI,HR,CU,CY,CZ,DK,DJ,DM,DO,EC,EG,SV,GQ,ER,EE,ET,FK,FO,FJ,
        FI,FR,GF,PF,TF,GA,GM,GE,DE,GH,GI,GR,GL,GD,GP,GU,GT,GG,GN,GW,GY,HT,HM,VA,
        HN,HK,HU,IS,IN,ID,IR,IQ,IE,IM,IL,IT,JM,JP,JE,JO,KZ,KE,KI,KP,KR,KW,KG,LA,
        LV,LB,LS,LR,LY,LI,LT,LU,MO,MK,MG,MW,MY,MV,ML,MT,MH,MQ,MR,MU,YT,MX,FM,MD,
        MC,MN,MS,MA,MZ,MM,NA,NR,NP,NL,AN,NC,NZ,NI,NE,NG,NU,NF,MP,NO,OM,PK,PW,PS,
        PA,PG,PY,PE,PH,PN,PL,PT,PR,QA,RE,RO,RU,RW,SH,KN,LC,PM,VC,WS,SM,ST,SA,SN,
        CS,SC,SL,SG,SK,SI,SB,SO,ZA,GS,ES,LK,SD,SR,SJ,SZ,SE,CH,SY,TW,TJ,TZ,TH,TL,
        TG,TK,TO,TT,TN,TR,TM,TC,TV,UG,UA,AE,GB,US,UM,UY,UZ,VU,VE,VN,VG,VI,WF,EH,
        YE,ZM,ZW
    }
}
