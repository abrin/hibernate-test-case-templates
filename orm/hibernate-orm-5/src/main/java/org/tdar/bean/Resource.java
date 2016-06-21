package org.tdar.bean;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * $Id$
 * 
 * Contains metadata common to all Resources.
 * 
 * Projects, Datasets, Documents, CodingSheets, Ontologies, SensoryData
 * 
 * @author <a href='Allen.Lee@asu.edu'>Allen Lee</a>
 * @version $Revision$
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL, region = "org.tdar.core.bean.resource.Resource")
@Table(name = "resource")
@Inheritance(strategy = InheritanceType.JOINED)
//@XmlRootElement
//@XmlSeeAlso({ Document.class, InformationResource.class, Project.class, CodingSheet.class, Dataset.class, Ontology.class,
//        Image.class, SensoryData.class, Video.class, Geospatial.class, Archive.class, Audio.class })
//@XmlAccessorType(XmlAccessType.PROPERTY)
//@XmlType(name = "resource", propOrder = {})
//@XmlTransient
public class Resource implements Persistable, Comparable<Resource>  {

    private static final long serialVersionUID = -230400285817185637L;


    protected final static transient Logger logger = LoggerFactory.getLogger(Resource.class);

    public Resource() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_sequence")
    @SequenceGenerator(name = "resource_sequence", allocationSize = 1, sequenceName = "resource_sequence")
    private Long id = -1L;

    @NotNull
    @Column(length = 512)
    @Length(max = 512)
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description_formatted")
    private String formattedDescription;

    
    
    @NotNull
    @Column(name = "date_created", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Length(max = 255)
    private String url;


    @NotNull
    @Column(name = "date_updated", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;


    // used by the import service to determine whether a record has been
    // "created" or updated
    // does not persist
    private transient boolean created = false;
    private transient boolean updated = false;

    @Column(name = "external_id")
    @Length(max = 255)
    private String externalId;

    private transient Float score = -1f;



    @Override
    @XmlAttribute
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateRegistered) {
        this.dateCreated = dateRegistered;
    }


    @Override
    public List<?> getEqualityFields() {
        return Collections.emptyList();
    }

    @Override
    public boolean equals(Object candidate) {
        return PersistableUtils.isEqual(this, (Persistable) candidate);
    }

    @Override
    public int hashCode() {
        return PersistableUtils.toHashCode(this);
    }

    /**
     * Returns the alphanumeric comparison of resource.title.
     */
    @Override
    public int compareTo(Resource resource) {
        int comparison = getTitle().compareTo(resource.getTitle());
        return (comparison == 0) ? getId().compareTo(resource.getId())
                : comparison;
    }

    public String getFormattedDescription() {
        return formattedDescription;
    }

    public void setFormattedDescription(String formattedDescription) {
        this.formattedDescription = formattedDescription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getDescription() {
        return description;
    }

}
