package org.tdar.bean;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

/**
 * $Id$
 * <p>
 * Represents a Resource with a file payload and additional metadata that can be one of the following:
 * </p>
 * <ol>
 * <li>Image
 * <li>Dataset file (Access, Excel)
 * <li>Document (PDF)
 * </ol>
 * 
 * @author <a href='Allen.Lee@asu.edu'>Allen Lee</a>
 * @version $Revision$
 */
@Entity
@Table(name = "information_resource")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class InformationResource extends Resource {

    public static final String LICENSE_TEXT = "LICENSE_TEXT";
    public static final String LICENSE_TYPE = "LICENSE_TYPE";
    public static final String COPYRIGHT_HOLDER = "COPYRIGHT_HOLDER";

    private static final long serialVersionUID = -1534799746444826257L;

    @Column(name = "license_text")
    @Type(type = "org.hibernate.type.TextType")
    @Lob
    private String licenseText;

    @Length(max = 255)
    @Column(name = "external_doi")
    private String doi;

    @Column(name = "external_reference", nullable = true)
    @XmlTransient
    private boolean externalReference;

    @Column(name = "copy_location")
    @Length(max = 255)
    private String copyLocation;

    @Column(name = "last_uploaded")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUploaded;

    // currently just a 4 digit year.
    @Column(name = "date_created")
    private Integer date = -1;

    @Column(name = "date_created_normalized")
    @XmlTransient
    private Integer dateNormalized = -1;

    // The institution providing this InformationResource
    @ManyToOne(optional = true, cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    @JoinColumn(name = "provider_institution_id")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Institution resourceProviderInstitution;

    @ManyToOne(optional = true, cascade = { CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH })
    @JoinColumn(name = "publisher_id")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Institution publisher;

    @Column(name = "publisher_location")
    @Length(max = 255)
    private String publisherLocation;


    public static final String INVESTIGATION_TYPE_INHERITANCE_TOGGLE = "inheriting_investigation_information";
    public static final String SITE_NAME_INHERITANCE_TOGGLE = "inheriting_site_information";
    public static final String MATERIAL_TYPE_INHERITANCE_TOGGLE = "inheriting_material_information";
    public static final String OTHER_INHERITANCE_TOGGLE = "inheriting_other_information";
    public static final String GEOGRAPHIC_INHERITANCE_TOGGLE = "inheriting_spatial_information";
    public static final String CULTURE_INHERITANCE_TOGGLE = "inheriting_cultural_information";
    public static final String TEMPORAL_INHERITANCE_TOGGLE = "inheriting_temporal_information";

    public String getLicenseText() {
        return licenseText;
    }

    // downward inheritance sections
    @Column(name = INVESTIGATION_TYPE_INHERITANCE_TOGGLE, nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingInvestigationInformation = false;
    @Column(name = SITE_NAME_INHERITANCE_TOGGLE, nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingSiteInformation = false;
    @Column(name = MATERIAL_TYPE_INHERITANCE_TOGGLE, nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingMaterialInformation = false;
    @Column(name = OTHER_INHERITANCE_TOGGLE, nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingOtherInformation = false;
    @Column(name = CULTURE_INHERITANCE_TOGGLE, nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingCulturalInformation = false;

    @Column(name = GEOGRAPHIC_INHERITANCE_TOGGLE, nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingSpatialInformation = false;

    @Column(name = TEMPORAL_INHERITANCE_TOGGLE, nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingTemporalInformation = false;

    @Column(name = "inheriting_note_information", nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingNoteInformation = false;

    @Column(name = "inheriting_identifier_information", nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingIdentifierInformation = false;

    @Column(name = "inheriting_collection_information", nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingCollectionInformation = false;

    @Column(name = "inheriting_individual_institutional_credit", nullable = false, columnDefinition = "boolean default FALSE")
    private boolean inheritingIndividualAndInstitutionalCredit = false;


    public void setLicenseText(String licenseText) {
        this.licenseText = licenseText;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer dateCreated) {
        this.date = dateCreated;
        if (dateCreated != null) {
            this.dateNormalized = (int) (Math.floor(dateCreated.floatValue() / 10f) * 10);
        } else {
            this.dateNormalized = null;
        }
    }

    public Integer getDateNormalized() {
        return dateNormalized;
    }

    @Deprecated
    public void setDateNormalized(Integer dateCreatedNormalized) {
        this.dateNormalized = dateCreatedNormalized;
    }

    public Institution getResourceProviderInstitution() {
        return resourceProviderInstitution;
    }

    public void setResourceProviderInstitution(Institution resourceProviderInstitution) {
        this.resourceProviderInstitution = resourceProviderInstitution;
    }


    /**
     * Returns true if this resource is an externally referenced resource,
     * signifying that there is no uploaded file. The URL should then
     * 
     * @return
     */
    public boolean isExternalReference() {
        return externalReference;
    }

    public void setExternalReference(boolean externalReference) {
        this.externalReference = externalReference;
    }

    public Date getLastUploaded() {
        return lastUploaded;
    }

    public void setLastUploaded(Date lastUploaded) {
        this.lastUploaded = lastUploaded;
    }
    public boolean isInheritingInvestigationInformation() {
        return inheritingInvestigationInformation;
    }

    public void setInheritingInvestigationInformation(boolean inheritingInvestigationInformation) {
        this.inheritingInvestigationInformation = inheritingInvestigationInformation;
    }

    public boolean isInheritingSiteInformation() {
        return inheritingSiteInformation;
    }

    public void setInheritingSiteInformation(boolean inheritingSiteInformation) {
        this.inheritingSiteInformation = inheritingSiteInformation;
    }

    public boolean isInheritingMaterialInformation() {
        return inheritingMaterialInformation;
    }

    public void setInheritingMaterialInformation(boolean inheritingMaterialInformation) {
        this.inheritingMaterialInformation = inheritingMaterialInformation;
    }

    public boolean isInheritingOtherInformation() {
        return inheritingOtherInformation;
    }

    public void setInheritingOtherInformation(boolean inheritingOtherInformation) {
        this.inheritingOtherInformation = inheritingOtherInformation;
    }

    public boolean isInheritingCulturalInformation() {
        return inheritingCulturalInformation;
    }

    public void setInheritingCulturalInformation(boolean inheritingCulturalInformation) {
        this.inheritingCulturalInformation = inheritingCulturalInformation;
    }

    public boolean isInheritingSpatialInformation() {
        return inheritingSpatialInformation;
    }

    public void setInheritingSpatialInformation(boolean inheritingSpatialInformation) {
        this.inheritingSpatialInformation = inheritingSpatialInformation;
    }

    public boolean isInheritingTemporalInformation() {
        return inheritingTemporalInformation;
    }

    public void setInheritingTemporalInformation(boolean inheritingTemporalInformation) {
        this.inheritingTemporalInformation = inheritingTemporalInformation;
    }

    public String getCopyLocation() {
        return copyLocation;
    }

    public void setCopyLocation(String copyLocation) {
        this.copyLocation = copyLocation;
    }



    @Transient
    @XmlTransient
    public boolean isInheritingSomeMetadata() {
        return (inheritingCulturalInformation || inheritingInvestigationInformation || inheritingMaterialInformation || inheritingOtherInformation ||
                inheritingSiteInformation || inheritingSpatialInformation || inheritingTemporalInformation || inheritingIdentifierInformation
                || inheritingNoteInformation || inheritingIndividualAndInstitutionalCredit);
    }

    public boolean isInheritingNoteInformation() {
        return inheritingNoteInformation;
    }

    public void setInheritingNoteInformation(boolean inheritingNoteInformation) {
        this.inheritingNoteInformation = inheritingNoteInformation;
    }

    public boolean isInheritingIdentifierInformation() {
        return inheritingIdentifierInformation;
    }

    public void setInheritingIdentifierInformation(boolean inheritingIdentifierInformation) {
        this.inheritingIdentifierInformation = inheritingIdentifierInformation;
    }

    public boolean isInheritingCollectionInformation() {
        return inheritingCollectionInformation;
    }

    public void setInheritingCollectionInformation(boolean inheritingCollectionInformation) {
        this.inheritingCollectionInformation = inheritingCollectionInformation;
    }



    public Institution getPublisher() {
        return publisher;
    }

    public void setPublisher(Institution publisher) {
        this.publisher = publisher;
    }

    public String getPublisherLocation() {
        return publisherLocation;
    }

    public void setPublisherLocation(String publisherLocation) {
        this.publisherLocation = publisherLocation;
    }

    public String getPublisherName() {
        if (publisher != null) {
            return publisher.getName();
        }
        return null;
    }

    public boolean isInheritingIndividualAndInstitutionalCredit() {
        return inheritingIndividualAndInstitutionalCredit;
    }

    public void setInheritingIndividualAndInstitutionalCredit(
            boolean inheritingIndividualAndInstitutionalCredit) {
        this.inheritingIndividualAndInstitutionalCredit = inheritingIndividualAndInstitutionalCredit;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

}
