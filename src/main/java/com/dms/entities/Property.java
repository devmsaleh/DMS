package com.dms.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.dms.enums.ColumnDatabaseTypeEnum;
import com.dms.enums.ColumnJavaTypeEnum;
import com.dms.enums.PropertyTypeEnum;

@Entity
public class Property implements Serializable {

	private static final long serialVersionUID = 7369620852479493766L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	private String displayNameArabic;

	private String displayNameEnglish;

	private String columnName;

	private boolean hidden;

	@OneToOne(fetch = FetchType.LAZY)
	private User createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateLastModified;

	@OneToOne(fetch = FetchType.LAZY)
	private User lastModifiedBy;

	private String type = PropertyTypeEnum.SMALL_TEXT.getValue();

	private String columnDatabaseType = ColumnDatabaseTypeEnum.SMALL_TEXT.getValue();

	private String columnJavaType = ColumnJavaTypeEnum.SMALL_TEXT.getValue();

	private boolean required = true;

	private int maxLength;

	@OneToOne(fetch = FetchType.LAZY)
	private ChoiceList choiceList;

	@Column(name = "choice_list_id", insertable = false, updatable = false)
	private Long choiceListId;

	private String symbolicName;

	@Transient
	private boolean documentTitle;

	@Transient
	private Object value;

	@Transient
	private boolean mainChoiceList;
	@Transient
	private boolean subChoiceList;
	@Transient
	private boolean normalChoiceList;
	@Transient
	private boolean hijriDate;
	@Transient
	private boolean gregDate;
	@Transient
	private boolean inputText;
	@Transient
	private boolean selectOneMenu;
	@Transient
	private boolean inputTextArea;
	@Transient
	private boolean showChoiceListInSearchAsInputText;
	@Transient
	private boolean longOnly;
	@Transient
	private boolean integerOnly;
	@Transient
	private boolean stringOnly;
	@Transient
	private List<ChoiceListItem> choiceListItemsList = new ArrayList<ChoiceListItem>();

	public Property() {

	}

	public Property(String columnName, Object value) {
		this.columnName = columnName;
		this.value = value;
		this.displayNameArabic = columnName;
	}

	public List<ChoiceListItem> getChoiceListItemsList() {
		return choiceListItemsList;
	}

	public void setChoiceListItemsList(List<ChoiceListItem> choiceListItemsList) {
		this.choiceListItemsList = choiceListItemsList;
	}

	public boolean isMainChoiceList() {
		return mainChoiceList;
	}

	public void setMainChoiceList(boolean mainChoiceList) {
		this.mainChoiceList = mainChoiceList;
	}

	public boolean isSubChoiceList() {
		return subChoiceList;
	}

	public void setSubChoiceList(boolean subChoiceList) {
		this.subChoiceList = subChoiceList;
	}

	public boolean isNormalChoiceList() {
		return normalChoiceList;
	}

	public void setNormalChoiceList(boolean normalChoiceList) {
		this.normalChoiceList = normalChoiceList;
	}

	public boolean isHijriDate() {
		return hijriDate;
	}

	public void setHijriDate(boolean hijriDate) {
		this.hijriDate = hijriDate;
	}

	public boolean isGregDate() {
		return gregDate;
	}

	public void setGregDate(boolean gregDate) {
		this.gregDate = gregDate;
	}

	public boolean isInputText() {
		return inputText;
	}

	public void setInputText(boolean inputText) {
		this.inputText = inputText;
	}

	public boolean isSelectOneMenu() {
		return selectOneMenu;
	}

	public void setSelectOneMenu(boolean selectOneMenu) {
		this.selectOneMenu = selectOneMenu;
	}

	public boolean isInputTextArea() {
		return inputTextArea;
	}

	public void setInputTextArea(boolean inputTextArea) {
		this.inputTextArea = inputTextArea;
	}

	public boolean isDocumentTitle() {
		return documentTitle;
	}

	public void setDocumentTitle(boolean documentTitle) {
		this.documentTitle = documentTitle;
	}

	public ChoiceList getChoiceList() {
		return choiceList;
	}

	public void setChoiceList(ChoiceList choiceList) {
		this.choiceList = choiceList;
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

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getMaxLength() {
		if (maxLength == 0) {
			maxLength = 1000;
		}
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColumnDatabaseType() {
		return columnDatabaseType;
	}

	public void setColumnDatabaseType(String columnDatabaseType) {
		this.columnDatabaseType = columnDatabaseType;
	}

	public String getColumnJavaType() {
		return columnJavaType;
	}

	public void setColumnJavaType(String columnJavaType) {
		this.columnJavaType = columnJavaType;
	}

	public String getSymbolicName() {
		return symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isShowChoiceListInSearchAsInputText() {
		return showChoiceListInSearchAsInputText;
	}

	public void setShowChoiceListInSearchAsInputText(boolean showChoiceListInSearchAsInputText) {
		this.showChoiceListInSearchAsInputText = showChoiceListInSearchAsInputText;
	}

	public boolean isLongOnly() {
		return longOnly;
	}

	public void setLongOnly(boolean longOnly) {
		this.longOnly = longOnly;
	}

	public boolean isIntegerOnly() {
		return integerOnly;
	}

	public void setIntegerOnly(boolean integerOnly) {
		this.integerOnly = integerOnly;
	}

	public boolean isStringOnly() {
		return stringOnly;
	}

	public void setStringOnly(boolean stringOnly) {
		this.stringOnly = stringOnly;
	}

	public Long getChoiceListId() {
		return choiceListId;
	}

	public void setChoiceListId(Long choiceListId) {
		this.choiceListId = choiceListId;
	}

}
