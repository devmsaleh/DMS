package com.dms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlPanelGrid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dms.dao.DocumentClassRepository;
import com.dms.dao.DocumentRepository;
import com.dms.dao.UtilsRepository;
import com.dms.entities.Document;
import com.dms.entities.DocumentClass;
import com.dms.enums.CustomColumnsEnum;
import com.dms.service.DocumentClassService;
import com.dms.util.ColumnModel;
import com.dms.util.GeneralUtils;
import com.dms.util.UIUtils;

@Component("searchDocumentBean")
@Scope("view")
public class SearchDocumentBean implements Serializable {

	private static final long serialVersionUID = -1179697707236061676L;

	private static final Logger log = LoggerFactory.getLogger(SearchDocumentBean.class);

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

	private List<String> propertiesWhereStatementsList = new ArrayList<String>(0);

	@Autowired
	private UtilsRepository utilsRepository;

	@Autowired
	private DocumentRepository documentRepository;

	private boolean renderSearchTable;

	private List<Document> documentsList = new ArrayList<Document>();

	private List<ColumnModel> columns = new ArrayList<ColumnModel>(0);

	private boolean searchPerformed;

	private Document selectedDocument;

	private String appURL;

	@PostConstruct
	public void init() {
		try {
			appURL = GeneralUtils.getAppURL();
			documentClassesList = documentClassRepository.findAll();
			if (documentClassesList.size() > 0) {
				selectedDocumentClassId = documentClassesList.get(0).getId();
				documentClassChanged();
			}
		} catch (Exception e) {
			log.error("Exception in init AddDocumentBean", e);
			throw new RuntimeException(e);
		}
	}

	public void documentClassChanged() {
		try {
			if (selectedDocumentClassId != null) {
				searchPerformed = false;
				documentsList.clear();
				selectedDocument = null;
				columns.clear();
				currentUserBean.getResultUUIDList().clear();
				selectedDocumentClass = documentClassService
						.findWithPropertiesAndChoiceListItems(selectedDocumentClassId);
				UIUtils.generatePropertiesInputsForSearch(selectedDocumentClass.getPropertiesList(),
						propertiesPanelGrid, localeBean.getLocale(), currentUserBean.getUser());
			}
		} catch (Exception e) {
			GeneralUtils.showSystemErrorDialog();
			log.error("Exception in documentClassChanged", e);
		}
	}

	public void shareDocument(Document document) {
		try {
			if (StringUtils.isBlank(document.getShareUUID())) {
				document.setShareUUID(UUID.randomUUID().toString());
				document.setSharedBy(currentUserBean.getUser());
				document.setSharedByFullName(currentUserBean.getUser().getDisplayName());
				document.setDateShared(new Date());
				documentRepository.save(document);
				selectedDocument = document;
			}
		} catch (Exception e) {
			GeneralUtils.showSystemErrorDialog();
			log.error("Exception in shareDocument", e);
		}
	}

	public void search() {
		try {
			if (selectedDocumentClassId == null) {
				GeneralUtils.addErrorMessage("يجب اختيار التصنيف أولا", null);
				return;
			}
			propertiesWhereStatementsList.clear();
			propertiesWhereStatementsList = UIUtils.prepareSeachProperties(propertiesPanelGrid,
					selectedDocumentClass.getPropertiesList());
			propertiesWhereStatementsList
					.add(CustomColumnsEnum.DOCUMENT_CLASS_ID.getValue() + " = " + selectedDocumentClassId);
			for (String where : propertiesWhereStatementsList) {
				System.out.println("####### where: " + where);
			}
			searchPerformed = true;

			int maxResult = 500;
			int pageSize = 10;
			documentsList = utilsRepository.findDocuments(selectedDocumentClass.getTableName().getValue(),
					selectedDocumentClass.getPropertiesList(),
					GeneralUtils.generateWhereStatementsString(propertiesWhereStatementsList),
					GeneralUtils.generateColumnsStringForSearch(selectedDocumentClass.getPropertiesList()), 1,
					maxResult, pageSize);
			System.out.println("########### documentsList: " + documentsList.size());
			renderSearchTable = documentsList.size() > 0;
			if (documentsList.size() > 0)
				columns = UIUtils.generateTableColumns(selectedDocumentClass.getPropertiesList());
			List<String> resultUUIDList = new ArrayList<String>();
			for (Document document : documentsList) {
				resultUUIDList.add(document.getUuid());
			}
			currentUserBean.setResultUUIDList(resultUUIDList);
			System.out.println("########### renderSearchTable: " + renderSearchTable);
			System.out.println("########### columns: " + columns.size());
		} catch (Exception e) {
			log.error("Exception in search,documentClass: " + selectedDocumentClass.getId() + ",tableName: "
					+ selectedDocumentClass.getTableName(), e);
			GeneralUtils.showSystemErrorDialog();
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

	public DocumentClass getSelectedDocumentClass() {
		return selectedDocumentClass;
	}

	public void setSelectedDocumentClass(DocumentClass selectedDocumentClass) {
		this.selectedDocumentClass = selectedDocumentClass;
	}

	public List<DocumentClass> getDocumentClassesList() {
		return documentClassesList;
	}

	public void setDocumentClassesList(List<DocumentClass> documentClassesList) {
		this.documentClassesList = documentClassesList;
	}

	public List<String> getPropertiesWhereStatementsList() {
		return propertiesWhereStatementsList;
	}

	public void setPropertiesWhereStatementsList(List<String> propertiesWhereStatementsList) {
		this.propertiesWhereStatementsList = propertiesWhereStatementsList;
	}

	public List<Document> getDocumentsList() {
		return documentsList;
	}

	public void setDocumentsList(List<Document> documentsList) {
		this.documentsList = documentsList;
	}

	public boolean isRenderSearchTable() {
		return renderSearchTable;
	}

	public void setRenderSearchTable(boolean renderSearchTable) {
		this.renderSearchTable = renderSearchTable;
	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnModel> columns) {
		this.columns = columns;
	}

	public boolean isSearchPerformed() {
		return searchPerformed;
	}

	public void setSearchPerformed(boolean searchPerformed) {
		this.searchPerformed = searchPerformed;
	}

	public Document getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(Document selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public String getAppURL() {
		return appURL;
	}

	public void setAppURL(String appURL) {
		this.appURL = appURL;
	}

}
