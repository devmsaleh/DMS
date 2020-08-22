package com.dms.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	private String originalFileName;
	private String fileName;
	private String mimeType;
	private String fullPath;
	private long contentSize;
	@OneToOne(fetch = FetchType.LAZY)
	private User createdBy;
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = new Date();
	@OneToOne(fetch = FetchType.LAZY)
	private DocumentClass documentClass;
	@Transient
	private Map<String, Object> customPropValues = new HashMap<String, Object>();

	@Column(nullable = false, unique = true)
	private String uuid = UUID.randomUUID().toString();

	@OneToOne(fetch = FetchType.LAZY)
	private User lastModifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateLastModified;

	private String shareUUID;

	@OneToOne(fetch = FetchType.LAZY)
	private User sharedBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateShared;

	public String getShareUUID() {
		return shareUUID;
	}

	public void setShareUUID(String shareUUID) {
		this.shareUUID = shareUUID;
	}

	public User getSharedBy() {
		return sharedBy;
	}

	public void setSharedBy(User sharedBy) {
		this.sharedBy = sharedBy;
	}

	public Date getDateShared() {
		return dateShared;
	}

	public void setDateShared(Date dateShared) {
		this.dateShared = dateShared;
	}

	public DocumentClass getDocumentClass() {
		return documentClass;
	}

	public void setDocumentClass(DocumentClass documentClass) {
		this.documentClass = documentClass;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public long getContentSize() {
		return contentSize;
	}

	public void setContentSize(long contentSize) {
		this.contentSize = contentSize;
	}

	public Map<String, Object> getCustomPropValues() {
		return customPropValues;
	}

	public void setCustomPropValues(Map<String, Object> customPropValues) {
		this.customPropValues = customPropValues;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public User getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

}
