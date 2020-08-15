package com.dms.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.dms.enums.DocumentClassTableNameEnum;

@Entity
public class DocumentClass extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8575974502268583654L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(nullable = false, unique = true)
	private String displayNameArabic;

	private String displayNameEnglish;

	private boolean hidden;

	private boolean deleted;

	@OneToOne(fetch = FetchType.LAZY)
	private User createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateLastModified;

	@OneToOne(fetch = FetchType.LAZY)
	private User lastModifiedBy;

	@OneToOne(fetch = FetchType.LAZY)
	private Property documentTitleProperty;

	@OneToOne(fetch = FetchType.LAZY)
	private DocumentClass parentDocumentClass;

	@Column(name = "document_title_property_id", insertable = false, updatable = false)
	private Long documentTitlePropertyId;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "document_class_properties", joinColumns = @JoinColumn(name = "document_class_id"), inverseJoinColumns = @JoinColumn(name = "property_id"))
	private List<Property> propertiesList = new ArrayList<Property>();

	@Enumerated(EnumType.STRING)
	private DocumentClassTableNameEnum tableName = DocumentClassTableNameEnum.DEFAULT_TABLE;

	private String symbolicName;

	public Property getDocumentTitleProperty() {
		return documentTitleProperty;
	}

	public void setDocumentTitleProperty(Property documentTitleProperty) {
		this.documentTitleProperty = documentTitleProperty;
	}

	public Long getDocumentTitlePropertyId() {
		return documentTitlePropertyId;
	}

	public void setDocumentTitlePropertyId(Long documentTitlePropertyId) {
		this.documentTitlePropertyId = documentTitlePropertyId;
	}

	public DocumentClass() {

	}

	public DocumentClass(Long id) {
		this.id = id;
	}

	public List<Property> getPropertiesList() {
		return propertiesList;
	}

	public void setPropertiesList(List<Property> propertiesList) {
		this.propertiesList = propertiesList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDisplayNameArabic() {
		return displayNameArabic;
	}

	public void setDisplayNameArabic(String displayNameArabic) {
		this.displayNameArabic = displayNameArabic;
	}

	public String getDisplayNameEnglish() {
		return displayNameEnglish;
	}

	public void setDisplayNameEnglish(String displayNameEnglish) {
		this.displayNameEnglish = displayNameEnglish;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public DocumentClassTableNameEnum getTableName() {
		return tableName;
	}

	public void setTableName(DocumentClassTableNameEnum tableName) {
		this.tableName = tableName;
	}

	public DocumentClass getParentDocumentClass() {
		return parentDocumentClass;
	}

	public void setParentDocumentClass(DocumentClass parentDocumentClass) {
		this.parentDocumentClass = parentDocumentClass;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

}
