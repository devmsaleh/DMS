package com.dms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlPanelGrid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dms.dao.UtilsRepository;
import com.dms.entities.Document;
import com.dms.entities.DocumentClass;
import com.dms.entities.Property;
import com.dms.enums.CustomColumnsEnum;
import com.dms.enums.PropertyTypeEnum;
import com.dms.service.DocumentClassService;
import com.dms.util.GeneralUtils;
import com.dms.util.UIUtils;
import com.dms.util.ViewerUtils;
import com.google.gson.Gson;

@Component("editDocumentBean")
@Scope("view")
public class EditDocumentBean implements Serializable {

	private static final long serialVersionUID = -6128402670533706878L;

	private static final Logger log = LoggerFactory.getLogger(EditDocumentBean.class);

	@Autowired
	private LocaleBean localeBean;

	@Autowired
	private CurrentUserBean currentUserBean;

	@Autowired
	private UtilsRepository utilsRepository;

	@Autowired
	private DocumentClassService documentClassService;

	private transient HtmlPanelGrid propertiesPanelGrid = new HtmlPanelGrid();

	private DocumentClass selectedDocumentClass;

	private Document document;

	private List<String> filesList = new ArrayList<String>();

	private boolean hideForm;

	public String getFilesListAsJson() {
		return new Gson().toJson(getFilesList());
	}

	@PostConstruct
	public void init() {
		try {
			String documentUUID = GeneralUtils.getHttpServletRequest().getParameter("id");
			loadDocument(documentUUID);
		} catch (Exception e) {
			log.error("Exception in init editDocumentBean", e);
			throw new RuntimeException(e);
		}
	}

	private void loadDocument(String documentUUID) throws Exception {

		log.info("####### EditDocumentBean >>> loadDocument,documentId: " + documentUUID);
		filesList.clear();
		document = null;
		if (StringUtils.isBlank(documentUUID)) {
			GeneralUtils.showDialogError("معرف الوثيقة غير موجود");
			return;
		}
		document = utilsRepository.findDocument(documentUUID);
		if (document == null) {
			GeneralUtils.showDialogError("الوثيقة غير موجودة");
			return;
		}
		selectedDocumentClass = documentClassService
				.findWithPropertiesAndChoiceListItems(document.getDocumentClass().getId());
		Map<String, Object> customPropertiesValues = document.getCustomPropValues();
		log.info("########## customPropertiesValues: " + customPropertiesValues.size());
		setPropertiesValues(selectedDocumentClass, customPropertiesValues);
		UIUtils.generatePropertiesInputsForAdd(selectedDocumentClass.getPropertiesList(), propertiesPanelGrid,
				localeBean.getLocale(), currentUserBean.getUser());
		if (document.getMimeType().contains("image")) {
			filesList = new ViewerUtils().doLoadImageIntoViewer(document);
		}
	}

	private void setPropertiesValues(DocumentClass documentClass, Map<String, Object> customPropertiesValues) {
		for (Property property : documentClass.getPropertiesList()) {
			Object value = customPropertiesValues.get(property.getSymbolicName());
			if (value != null) {
				if (StringUtils.isNotBlank(value.toString())
						&& property.getType().equalsIgnoreCase(PropertyTypeEnum.MULTI_TEXT.toString())) {
					List<String> valuesList = Arrays.asList(value.toString().split("#"));
					List<String> finalList = new ArrayList<String>();
					for (String str : valuesList) {
						if (StringUtils.isNotBlank(str))
							finalList.add(str);
					}
					property.setValue(finalList);
				} else {
					property.setValue(value.toString());
				}
			}
		}
	}

	public void updateDocument() {
		try {
			log.info("####### updateDocument #########");
			UIUtils.getPropertiesInputsValues(propertiesPanelGrid, selectedDocumentClass.getSymbolicName(),
					selectedDocumentClass.getPropertiesList(), null);

			setCustomProperties();
			utilsRepository.updateDocument(selectedDocumentClass.getTableName().getValue(),
					selectedDocumentClass.getPropertiesList(), document.getId());
			removeCustomProperties();

			for (Property property : selectedDocumentClass.getPropertiesList()) {
				property.setValue("");
			}
			UIUtils.clearPrpertiesInputs(propertiesPanelGrid);
			filesList.clear();
			GeneralUtils.addInfoMessage("تم الحفظ", null);
			int index = currentUserBean.getResultUUIDList().indexOf(document.getUuid());
			document = null;
			if ((index + 1) != currentUserBean.getResultUUIDList().size()) {
				String nextUUID = currentUserBean.getResultUUIDList().get(index + 1);
				loadDocument(nextUUID);
			} else {
				hideForm = true; // we should bind this to form
				propertiesPanelGrid.getChildren().clear();
				GeneralUtils.showDialogError("تم تعديل جميع نتائج البحث");
			}
		} catch (Exception e) {
			log.error("Exception in init updateDocument", e);
			GeneralUtils.showSystemErrorDialog();
		}
	}

	private void setCustomProperties() {
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.LAST_MODIFIED_BY_ID.getValue(), currentUserBean.getUser().getId()));
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.DATE_LAST_MODIFIED.getValue(), GeneralUtils.getCurrentTimeMYSQL()));
	}

	private void removeCustomProperties() {
		for (Iterator<Property> iterator = selectedDocumentClass.getPropertiesList().iterator(); iterator.hasNext();) {
			Property property = iterator.next();
			if (property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.LAST_MODIFIED_BY_ID.getValue())
					|| property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.DATE_LAST_MODIFIED.getValue())) {
				iterator.remove();
			}
		}
	}

	public HtmlPanelGrid getPropertiesPanelGrid() {
		return propertiesPanelGrid;
	}

	public void setPropertiesPanelGrid(HtmlPanelGrid propertiesPanelGrid) {
		this.propertiesPanelGrid = propertiesPanelGrid;
	}

	public DocumentClass getSelectedDocumentClass() {
		return selectedDocumentClass;
	}

	public void setSelectedDocumentClass(DocumentClass selectedDocumentClass) {
		this.selectedDocumentClass = selectedDocumentClass;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public List<String> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<String> filesList) {
		this.filesList = filesList;
	}

	public boolean isHideForm() {
		return hideForm;
	}

	public void setHideForm(boolean hideForm) {
		this.hideForm = hideForm;
	}

}
