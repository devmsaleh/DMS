package com.dms.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlPanelGrid;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dms.FileStore;
import com.dms.dao.DocumentClassRepository;
import com.dms.dao.UtilsRepository;
import com.dms.entities.Attachment;
import com.dms.entities.DocumentClass;
import com.dms.entities.Property;
import com.dms.enums.CustomColumnsEnum;
import com.dms.service.DocumentClassService;
import com.dms.util.Constants;
import com.dms.util.CustomFileUtils;
import com.dms.util.GeneralUtils;
import com.dms.util.UIUtils;

@Component("addDocumentBean")
@Scope("view")
public class AddDocumentBean implements Serializable {

	private static final long serialVersionUID = 7827864578714382953L;

	private static final Logger log = LoggerFactory.getLogger(AddDocumentBean.class);

	private transient HtmlPanelGrid propertiesPanelGrid = new HtmlPanelGrid();

	private Long selectedDocumentClassId;

	private DocumentClass selectedDocumentClass;

	private List<DocumentClass> documentClassesList = new ArrayList<DocumentClass>();

	@Autowired
	private DocumentClassService documentClassService;

	@Autowired
	private DocumentClassRepository documentClassRepository;

	@Autowired
	private LocaleBean localeBean;

	@Autowired
	private CurrentUserBean currentUserBean;

	@Autowired
	private UtilsRepository utilsRepository;

	private List<Attachment> filesList = new ArrayList<Attachment>();

	@PostConstruct
	public void init() {
		try {
			documentClassesList = documentClassRepository.findAll();
			if (documentClassesList.size() > 0) {
				selectedDocumentClassId = documentClassesList.get(0).getId();
				documentClassChanged();
			}
		} catch (Exception e) {
			log.error("Exception in init AddDocumentBean", e);
		}
	}

	public void documentClassChanged() {
		try {
			selectedDocumentClass = documentClassService.findWithPropertiesAndChoiceListItems(selectedDocumentClassId);
			UIUtils.generatePropertiesInputs(selectedDocumentClass.getPropertiesList(), propertiesPanelGrid,
					localeBean.getLocale(), Constants.OPERATION_ADD_DOCUMENT, currentUserBean.getUser());
		} catch (Exception e) {
			PrimeFaces.current().ajax().addCallbackParam("errorDialog", true);
			log.error("Exception in documentClassChanged", e);
		}
	}

	public void createNewDocument() {
		try {

			if (filesList.size() == 0) {
				GeneralUtils.addErrorMessage("يجب رفع الملف أولا", null);
				return;
			}

			Map<String, Object> propertiesMap = UIUtils.getPropertiesInputsValues(propertiesPanelGrid,
					selectedDocumentClass.getSymbolicName(), null);

			for (Property property : selectedDocumentClass.getPropertiesList()) {
				Object valueObj = propertiesMap.get(property.getSymbolicName());
				if (valueObj != null)
					property.setValue(valueObj);
			}

			setCustomProperties();
			utilsRepository.insertDocument(selectedDocumentClass.getTableName().getValue(),
					selectedDocumentClass.getPropertiesList());
			removeCustomProperties();
			for (Property property : selectedDocumentClass.getPropertiesList()) {
				property.setValue("");
			}
			UIUtils.clearPrpertiesInputs(propertiesPanelGrid);
			filesList.clear();
			PrimeFaces.current().ajax().addCallbackParam("successDialog", true);
		} catch (Exception e) {
			PrimeFaces.current().ajax().addCallbackParam("errorDialog", true);
			log.error("Exception in init createNewDocument", e);
		}
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {

		try {

			if (filesList.size() == 1) {
				GeneralUtils.addErrorMessage("لا يمكن رفع أكثر من ملف واحد", null);
				return;
			}

			UploadedFile uploadedFile = event.getFile();
			Attachment attachment = new Attachment();
			attachment.setContentSize(uploadedFile.getSize());
			attachment.setFileName(CustomFileUtils.generateRandomFileName(uploadedFile.getFileName()));
			attachment.setFullPath(CustomFileUtils.generateRandomStoreFolderPath() + attachment.getFileName());
			attachment.setMimeType(uploadedFile.getContentType());
			attachment.setOriginalFileName(uploadedFile.getFileName());
			Files.write(new File(attachment.getFullPath()).toPath(), uploadedFile.getContents());
			filesList.add(attachment);
			GeneralUtils.addInfoMessage("تم رفع الملف بنجاح", null);
		} catch (NoSuchFileException nfe) {
			FileStore.createStoreFolders("C:\\FileStore");
			handleFileUpload(event);
		} catch (Exception e) {
			log.error("Exception in handleFileUpload", e);
			GeneralUtils.addErrorMessage("حدث خطأ فى النظام من فضلك حاول مرة أخرى لاحقا", null);
		}

	}

	private void setCustomProperties() {
		selectedDocumentClass.getPropertiesList().add(new Property(CustomColumnsEnum.CONTENT_SIZE.getValue(), 0));
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.DOCUMENT_CLASS_ID.getValue(), selectedDocumentClass.getId()));
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.CREATED_BY_ID.getValue(), currentUserBean.getUser().getId()));
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.DATE_CREATED.getValue(), GeneralUtils.getCurrentTimeMYSQL()));
		Attachment attachment = filesList.get(0);
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.FILE_NAME.getValue(), attachment.getFileName()));
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.FULL_PATH.getValue(), attachment.getFullPath()));
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.MIME_TYPE.getValue(), attachment.getMimeType()));
		selectedDocumentClass.getPropertiesList()
				.add(new Property(CustomColumnsEnum.ORIGINAL_FILE_NAME.getValue(), attachment.getOriginalFileName()));
	}

	private void removeCustomProperties() {
		for (Iterator<Property> iterator = selectedDocumentClass.getPropertiesList().iterator(); iterator.hasNext();) {
			Property property = iterator.next();
			if (property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.CONTENT_SIZE.getValue())
					|| property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.DOCUMENT_CLASS_ID.getValue())
					|| property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.CREATED_BY_ID.getValue())
					|| property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.DATE_CREATED.getValue())
					|| property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.FILE_NAME.getValue())
					|| property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.FULL_PATH.getValue())
					|| property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.MIME_TYPE.getValue())
					|| property.getColumnName().equalsIgnoreCase(CustomColumnsEnum.ORIGINAL_FILE_NAME.getValue())) {
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

	public Long getSelectedDocumentClassId() {
		return selectedDocumentClassId;
	}

	public void setSelectedDocumentClassId(Long selectedDocumentClassId) {
		this.selectedDocumentClassId = selectedDocumentClassId;
	}

	public List<DocumentClass> getDocumentClassesList() {
		return documentClassesList;
	}

	public void setDocumentClassesList(List<DocumentClass> documentClassesList) {
		this.documentClassesList = documentClassesList;
	}

	public DocumentClass getSelectedDocumentClass() {
		return selectedDocumentClass;
	}

	public void setSelectedDocumentClass(DocumentClass selectedDocumentClass) {
		this.selectedDocumentClass = selectedDocumentClass;
	}

	public List<Attachment> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<Attachment> filesList) {
		this.filesList = filesList;
	}

}
