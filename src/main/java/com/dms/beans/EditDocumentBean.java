package com.dms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.dms.dao.DocumentRepository;
import com.dms.dao.UtilsRepository;
import com.dms.entities.Document;
import com.dms.entities.DocumentClass;
import com.dms.entities.Property;
import com.dms.enums.PropertyTypeEnum;
import com.dms.util.GeneralUtils;
import com.dms.util.UIUtils;

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
	private DocumentRepository documentRepository;

	@Autowired
	private UtilsRepository utilsRepository;

	private transient HtmlPanelGrid propertiesPanelGrid = new HtmlPanelGrid();

	private DocumentClass selectedDocumentClass;

	private Document document;

	@PostConstruct
	public void init() {
		try {
			String documentId = GeneralUtils.getHttpServletRequest().getParameter("id");
			log.info("####### INIT EditDocumentBean,documentId: " + documentId);
			if (StringUtils.isBlank(documentId)) {
				GeneralUtils.showDialogError("معرف الوثيقة غير موجود");
				return;
			}
			document = utilsRepository.findDocument(documentId);
			if (document == null) {
				GeneralUtils.showDialogError("الوثيقة غير موجودة");
				return;
			}
			selectedDocumentClass = document.getDocumentClass();
			Map<String, Object> customPropertiesValues = document.getCustomPropValues();
			log.info("########## customPropertiesValues: " + customPropertiesValues.size());
			setPropertiesValues(selectedDocumentClass, customPropertiesValues);
			UIUtils.generatePropertiesInputsForAdd(selectedDocumentClass.getPropertiesList(), propertiesPanelGrid,
					localeBean.getLocale(), currentUserBean.getUser());
		} catch (Exception e) {
			log.error("Exception in init editDocumentBean", e);
			throw new RuntimeException(e);
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
		} catch (Exception e) {
			log.error("Exception in init updateDocument", e);
			GeneralUtils.showSystemErrorDialog();
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

}
