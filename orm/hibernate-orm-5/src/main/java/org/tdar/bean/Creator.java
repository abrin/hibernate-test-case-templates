package org.tdar.bean;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * $Id$
 * 
 * Base class for Persons and Institutions that can be assigned as a
 * ResourceCreator.
 * 
 * 
 * @author <a href='mailto:allen.lee@asu.edu'>Allen Lee</a>
 * @version $Rev$
 */
@Entity
@Table(name = "creator")
@Inheritance(strategy = InheritanceType.JOINED)
@XmlSeeAlso({ Institution.class })
@XmlAccessorType(XmlAccessType.PROPERTY)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "org.tdar.core.bean.entity.Creator")
public abstract class Creator<T extends Creator<?>> implements Persistable  {

    protected final static transient Logger logger = LoggerFactory.getLogger(Creator.class);
    private transient boolean obfuscated;
    private transient Boolean obfuscatedObjectDifferent;
    public static final String OCCURRENCE = "occurrence";
    public static final String BROWSE_OCCURRENCE = "browse_occurrence";

    private static final long serialVersionUID = 2296217124845743224L;

    public enum CreatorType {
        PERSON("P"), INSTITUTION("I");
        private String code;

        private CreatorType(String code) {
            this.code = code;
        }

        
        public String getCode() {
            return this.code;
        }

        public boolean isPerson() {
            return this == PERSON;
        }

        public boolean isInstitution() {
            return this == INSTITUTION;
        }

    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = -1L;
    /*
     * @Boost(.5f)
     * 
     * //@IndexedEmbedded
     * 
     * @ManyToOne()
     * 
     * @JoinColumn(name = "updater_id")
     * private Person updatedBy;
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated", nullable = false)
    @NotNull
    private Date dateUpdated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable=false)
    @NotNull
    private Date dateCreated;


    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}, targetEntity=Creator.class, orphanRemoval=true)
    @JoinColumn(name = "merge_creator_id")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<T> synonyms = new HashSet<>();

    
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    public Creator() {
        setDateCreated(new Date());
        setDateUpdated(new Date());
    }

    @Column(length = 255)
    @Length(max = 255)
    private String url;

//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(nullable = false, updatable = true, name = "creator_id")
//    @NotNull
//    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
//    private Set<Address> addresses = new LinkedHashSet<>();


    public abstract String getName();

    
    
    public String getLabel() {
    	return getName();
    }

    public abstract String getProperName();


    @Override
    @XmlAttribute
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public abstract CreatorType getCreatorType();

    @Override
    public boolean equals(Object candidate) {
        if (candidate == null || !(candidate instanceof Creator)) {
            return false;
        }
        try {
            return PersistableUtils.isEqual(this, Creator.class.cast(candidate));
        } catch (ClassCastException e) {
            logger.debug("cannot cast creator: ", e);
            return false;
        }
    }

    // private transient int hashCode = -1;

    /*
     * copied from PersistableUtils.hashCode() (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        Logger logger = LoggerFactory.getLogger(getClass());
        int hashCode = -1;
        if (PersistableUtils.isNullOrTransient(this)) {
            hashCode = super.hashCode();
        } else {
            hashCode = PersistableUtils.toHashCode(this);
        }

        Object[] obj = { hashCode, getClass().getSimpleName(), getId() };
        logger.trace("setting hashCode to {} ({}) {}", obj);
        return hashCode;
    }

    @Override
    @XmlTransient
    public List<?> getEqualityFields() {
        return Collections.emptyList();
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }


    @XmlElementWrapper(name = "synonyms")
    @XmlElement(name = "synonymRef")
    public Set<T> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<T> synonyms) {
        this.synonyms = synonyms;
    }

    @XmlTransient
    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getTitle() {
        return getProperName();
    }

    public Date getDateCreated() {
        return dateCreated;
    }
}
