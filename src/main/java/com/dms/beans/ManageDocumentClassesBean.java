package com.dms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dms.dao.ChoiceListItemRepository;
import com.dms.dao.ChoiceListRepository;
import com.dms.dao.DocumentClassRepository;
import com.dms.dao.PropertyRepository;
import com.dms.dao.UtilsRepository;
import com.dms.entities.ChoiceList;
import com.dms.entities.ChoiceListItem;
import com.dms.entities.DocumentClass;
import com.dms.entities.Property;
import com.dms.enums.DocumentClassTableNameEnum;
import com.dms.enums.PropertyTypeEnum;
import com.dms.service.DocumentClassService;
import com.dms.util.Constants;
import com.dms.util.GeneralUtils;

@Component("manageDocumentClassesBean")
@Scope("view")
public class ManageDocumentClassesBean implements Serializable {

	private static final long serialVersionUID = 7175082133716091269L;

	private static final Logger log = LoggerFactory.getLogger(ManageDocumentClassesBean.class);

	@Autowired
	private DocumentClassRepository documentClassRepository;

	@Autowired
	private DocumentClassService documentClassService;

	@Autowired
	private PropertyRepository propertyRepository;

	@Autowired
	private CurrentUserBean currentUserBean;

	@Autowired
	private UtilsRepository utilsRepository;

	@Autowired
	private ChoiceListRepository choiceListRepository;

	@Autowired
	private ChoiceListItemRepository choiceListItemRepository;

	private DocumentClass newDocumentClass = new DocumentClass();

	private ChoiceList newChoiceList = new ChoiceList();

	private List<DocumentClass> documentClassesList = new ArrayList<DocumentClass>();

	private List<ChoiceList> choiceLists = new ArrayList<ChoiceList>();

	private DocumentClass selectedDocumentClass;

	private Long propertyChoiceListId;

	private ChoiceList selectedChoiceList;

	private String newItemName;

	private Property newProperty = new Property();

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleBean localeBean;

	private final String PROPERTY_TYPE_CHOICELIST = PropertyTypeEnum.NORMAL_CHOICELIST.getValue();

	@PostConstruct
	public void init() {
		try {
			documentClassesList = documentClassRepository.findAll();
			if (documentClassesList.size() > 0) {
				selectedDocumentClass = documentClassesList.get(0);
			}
			choiceLists = choiceListRepository.findAll();
			if (choiceLists.size() > 0) {
				// selectedChoiceList = choiceLists.get(0);
			}
		} catch (Exception e) {
			log.error("Exception in init ManageDocumentClassesBean", e);
			throw new RuntimeException(e);
		}
	}

	public void saveDocumentClass() {

		newDocumentClass.setDisplayNameArabic(newDocumentClass.getDisplayNameArabic().trim());

		if (StringUtils.isBlank(newDocumentClass.getDisplayNameEnglish())) {
			newDocumentClass.setDisplayNameEnglish(newDocumentClass.getDisplayNameArabic());
		}

		if (newDocumentClass.getId() == null || newDocumentClass.getId() <= 0) {
			createNewDocumentClass();
		} else {
			updateDocumentClass();
		}
	}

	private void updateDocumentClass() {
		try {

			newDocumentClass.setLastModifiedBy(currentUserBean.getUser());
			newDocumentClass.setDateLastModified(new Date());
			documentClassRepository.save(newDocumentClass);
			newDocumentClass = new DocumentClass();
			PrimeFaces.current().executeScript("PF('successDialogWidget').show()");
		} catch (Exception e) {
			GeneralUtils.showSystemErrorDialog();
			log.error("Exception in createNewDocumentClass", e);
		}
	}

	private void createNewDocumentClass() {
		try {

			if (documentClassRepository
					.countByDisplayNameArabicIgnoreCase(newDocumentClass.getDisplayNameArabic().trim()) > 0) {
				GeneralUtils.addErrorMessage("يوجد تصنيف بنفس الإسم العربى مدخل مسبقا", null);
				return;
			}

			newDocumentClass.setCreatedBy(currentUserBean.getUser());
			newDocumentClass.setLastModifiedBy(currentUserBean.getUser());
			newDocumentClass.setSymbolicName(generateDocumentClassSymbolicName());
			documentClassRepository.save(newDocumentClass);
			DocumentClass newDocumentClassCopy = SerializationUtils.clone(newDocumentClass);
			documentClassesList.add(newDocumentClassCopy);
			newDocumentClass = new DocumentClass();
			PrimeFaces.current().executeScript("PF('successDialogWidget').show()");
		} catch (Exception e) {
			GeneralUtils.showSystemErrorDialog();
			log.error("Exception in createNewDocumentClass", e);
		}
	}

	public void addNewDocumentClassButtonClicked() {
		newDocumentClass = new DocumentClass();
	}

	public void setSelectedDocumentClass() {
		newDocumentClass = selectedDocumentClass;
	}

	public void createNewChoiceList() {
		try {

			if (StringUtils.isBlank(newChoiceList.getDisplayNameArabic())) {
				GeneralUtils.addErrorMessage("يرجى ادخال اسم القائمة أولا", null);
				return;
			}

			newChoiceList.setDisplayNameArabic(newChoiceList.getDisplayNameArabic().trim());

			if (choiceListRepository.countByDisplayNameArabicIgnoreCase(newChoiceList.getDisplayNameArabic()) > 0) {
				GeneralUtils.addErrorMessage("يوجد قائمة بنفس الإسم العربى مدخلة مسبقا", null);
				return;
			}
			if (StringUtils.isBlank(newChoiceList.getDisplayNameEnglish())) {
				newChoiceList.setDisplayNameEnglish(newChoiceList.getDisplayNameArabic());
			}
			newChoiceList.setCreatedBy(currentUserBean.getUser());
			newChoiceList.setLastModifiedBy(currentUserBean.getUser());
			choiceListRepository.save(newChoiceList);
			ChoiceList newChoiceListCopy = SerializationUtils.clone(newChoiceList);
			choiceLists.add(newChoiceListCopy);
			selectedChoiceList = newChoiceListCopy;
			newChoiceList = new ChoiceList();
			PrimeFaces.current().executeScript("PF('successDialogWidget').show()");
		} catch (Exception e) {
			GeneralUtils.showSystemErrorDialog();
			log.error("Exception in createNewChoiceList", e);
		}
	}

	public void createNewItem() {
		try {
			if (selectedChoiceList == null) {
				GeneralUtils.addErrorMessage("يرجى اختيار القائمة المراد الإضافة لها أولا", null);
				return;
			}
			if (StringUtils.isNotBlank(newItemName)) {
				newItemName = newItemName.trim();
				if (isItemExistsInChoiceList(newItemName, selectedChoiceList.getItemsList())) {
					GeneralUtils.addErrorMessage("العنصر موجود مسبقا فى القائمة", null);
					return;
				}
				ChoiceListItem choiceListItem = new ChoiceListItem(selectedChoiceList, newItemName);
				selectedChoiceList.getItemsList().add(choiceListItem);
				choiceListItemRepository.save(choiceListItem);
				newItemName = "";
			} else {
				GeneralUtils.addErrorMessage("يرجى ادخال العنصر أولا", null);
				return;
			}
			// PrimeFaces.current().ajax().addCallbackParam("successDialog", true);
		} catch (Exception e) {
			GeneralUtils.showSystemErrorDialog();
			log.error("Exception in createNewProperty", e);
		}
	}

	private boolean isItemExistsInChoiceList(String itemName, Set<ChoiceListItem> choiceListItemsList) {
		for (ChoiceListItem itemLoop : choiceListItemsList) {
			if (itemLoop.getValue().equalsIgnoreCase(itemName))
				return true;
		}
		return false;
	}

	public void createNewProperty() {
		try {

			doCreateNewProperty();
			PrimeFaces.current().executeScript("PF('successDialogWidget').show()");
		} catch (Exception e) {
			GeneralUtils.showSystemErrorDialog();
			log.error("Exception in createNewProperty", e);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	private void doCreateNewProperty() {

		System.out.println("####### propertyChoiceListId: " + propertyChoiceListId);

		if ((newProperty.getType().equals(PropertyTypeEnum.NORMAL_CHOICELIST.getValue())
				|| newProperty.getType().equals(PropertyTypeEnum.MAIN_CHOICELIST.getValue()))
				&& propertyChoiceListId == null) {
			GeneralUtils.addErrorMessage("يرجى اختيار القائمة", null);
			return;
		}

		newProperty.setCreatedBy(currentUserBean.getUser());
		newProperty.setColumnDatabaseType(decideColumnDatabaseType(newProperty.getType()));
		newProperty.setColumnJavaType(decideColumnJavaType(newProperty.getType()));
		newProperty.setColumnName(generateColumnName());
		newProperty.setSymbolicName(generatePropertySymbolicName());
		// Property newPropertyCopy = SerializationUtils.clone(newProperty);
		if (propertyChoiceListId != null) {
			newProperty.setChoiceList(new ChoiceList(propertyChoiceListId));
		}
		propertyRepository.save(newProperty);
		utilsRepository.createNewDocumentColumn(DocumentClassTableNameEnum.DEFAULT_TABLE.getValue(),
				newProperty.getColumnName(), newProperty.getColumnDatabaseType());

		selectedDocumentClass.getPropertiesList().add(newProperty);
		documentClassRepository.save(selectedDocumentClass);

		propertyChoiceListId = null;
		newProperty = new Property();
	}

	private String generateColumnName() {
		String columnName = null;
		for (int i = 0; i < 50; i++) {
			columnName = Constants.COLUMN_NAME_PREFIX + RandomStringUtils.randomNumeric(5);
			if (propertyRepository.countByColumnNameIgnoreCase(columnName) == 0)
				return columnName;
		}
		return null;
	}

	private String generateDocumentClassSymbolicName() {
		String name = null;
		for (int i = 0; i < 50; i++) {
			name = Constants.DOCUMENT_CLASS_NAME_PREFIX + RandomStringUtils.randomNumeric(5);
			if (documentClassRepository.countBySymbolicNameIgnoreCase(name) == 0)
				return name;
		}
		return null;
	}

	private String generatePropertySymbolicName() {
		String name = null;
		for (int i = 0; i < 50; i++) {
			name = Constants.PROPERTY_NAME_PREFIX + RandomStringUtils.randomNumeric(5);
			if (propertyRepository.countBySymbolicNameIgnoreCase(name) == 0)
				return name;
		}
		return null;
	}

	public String getPropertyTypeLabel(String propertyType) {
		if (StringUtils.isBlank(propertyType))
			return "";
		String value = messageSource.getMessage(propertyType, null, localeBean.getLocale());
		if (value != null) {
			value = value.trim();
		}
		return value;
	}

	private String decideColumnDatabaseType(String propertyType) {
		String columnDatabaseType = "";
		if (propertyType.equals(PropertyTypeEnum.GREG_DATE.getValue())) {
			columnDatabaseType = PropertyTypeEnum.GREG_DATE.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.HIJRI_DATE.getValue())) {
			columnDatabaseType = PropertyTypeEnum.HIJRI_DATE.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.LONG_NUMBER.getValue())) {
			columnDatabaseType = PropertyTypeEnum.LONG_NUMBER.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.LONG_TEXT.getValue())) {
			columnDatabaseType = PropertyTypeEnum.LONG_TEXT.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.MAIN_CHOICELIST.getValue())) {
			columnDatabaseType = PropertyTypeEnum.MAIN_CHOICELIST.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.NORMAL_CHOICELIST.getValue())) {
			columnDatabaseType = PropertyTypeEnum.NORMAL_CHOICELIST.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.SMALL_NUMBER.getValue())) {
			columnDatabaseType = PropertyTypeEnum.SMALL_NUMBER.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.SMALL_TEXT.getValue())) {
			columnDatabaseType = PropertyTypeEnum.SMALL_TEXT.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.SUB_CHOICELIST.getValue())) {
			columnDatabaseType = PropertyTypeEnum.SUB_CHOICELIST.getDatabaseType();
		} else if (propertyType.equals(PropertyTypeEnum.MULTI_TEXT.getValue())) {
			columnDatabaseType = PropertyTypeEnum.MULTI_TEXT.getDatabaseType();
		}
		return columnDatabaseType;
	}

	private String decideColumnJavaType(String propertyType) {
		String columnJavaType = "";
		if (propertyType.equals(PropertyTypeEnum.GREG_DATE.getValue())) {
			columnJavaType = PropertyTypeEnum.GREG_DATE.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.HIJRI_DATE.getValue())) {
			columnJavaType = PropertyTypeEnum.HIJRI_DATE.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.LONG_NUMBER.getValue())) {
			columnJavaType = PropertyTypeEnum.LONG_NUMBER.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.LONG_TEXT.getValue())) {
			columnJavaType = PropertyTypeEnum.LONG_TEXT.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.MAIN_CHOICELIST.getValue())) {
			columnJavaType = PropertyTypeEnum.MAIN_CHOICELIST.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.NORMAL_CHOICELIST.getValue())) {
			columnJavaType = PropertyTypeEnum.NORMAL_CHOICELIST.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.SMALL_NUMBER.getValue())) {
			columnJavaType = PropertyTypeEnum.SMALL_NUMBER.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.SMALL_TEXT.getValue())) {
			columnJavaType = PropertyTypeEnum.SMALL_TEXT.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.SUB_CHOICELIST.getValue())) {
			columnJavaType = PropertyTypeEnum.SUB_CHOICELIST.getJavaType();
		} else if (propertyType.equals(PropertyTypeEnum.MULTI_TEXT.getValue())) {
			columnJavaType = PropertyTypeEnum.MULTI_TEXT.getJavaType();
		}
		return columnJavaType;
	}

	public DocumentClass getNewDocumentClass() {
		return newDocumentClass;
	}

	public void setNewDocumentClass(DocumentClass newDocumentClass) {
		this.newDocumentClass = newDocumentClass;
	}

	public List<DocumentClass> getDocumentClassesList() {
		return documentClassesList;
	}

	public void setDocumentClassesList(List<DocumentClass> documentClassesList) {
		this.documentClassesList = documentClassesList;
	}

	public Property getNewProperty() {
		return newProperty;
	}

	public void setNewProperty(Property newProperty) {
		this.newProperty = newProperty;
	}

	public DocumentClass getSelectedDocumentClass() {
		return selectedDocumentClass;
	}

	public void setSelectedDocumentClass(DocumentClass selectedDocumentClass) {
		this.selectedDocumentClass = selectedDocumentClass;
	}

	public List<ChoiceList> getChoiceLists() {
		return choiceLists;
	}

	public void setChoiceLists(List<ChoiceList> choiceLists) {
		this.choiceLists = choiceLists;
	}

	public ChoiceList getSelectedChoiceList() {
		return selectedChoiceList;
	}

	public void setSelectedChoiceList(ChoiceList selectedChoiceList) {
		this.selectedChoiceList = selectedChoiceList;
	}

	public ChoiceList getNewChoiceList() {
		return newChoiceList;
	}

	public void setNewChoiceList(ChoiceList newChoiceList) {
		this.newChoiceList = newChoiceList;
	}

	public String getNewItemName() {
		return newItemName;
	}

	public void setNewItemName(String newItemName) {
		this.newItemName = newItemName;
	}

	public Long getPropertyChoiceListId() {
		return propertyChoiceListId;
	}

	public void setPropertyChoiceListId(Long propertyChoiceListId) {
		this.propertyChoiceListId = propertyChoiceListId;
	}

	public String getPROPERTY_TYPE_CHOICELIST() {
		return PROPERTY_TYPE_CHOICELIST;
	}

}
