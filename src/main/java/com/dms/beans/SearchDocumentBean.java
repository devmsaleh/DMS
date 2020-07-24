package com.dms.beans;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlPanelGrid;

import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dms.dao.DocumentClassRepository;
import com.dms.dao.UtilsRepository;
import com.dms.entities.Document;
import com.dms.entities.DocumentClass;
import com.dms.enums.CustomColumnsEnum;
import com.dms.service.DocumentClassService;
import com.dms.util.ColumnModel;
import com.dms.util.Constants;
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

    private boolean renderSearchTable;

    private List<Document> documentsList = new ArrayList<Document>();

    private List<ColumnModel> columns = new ArrayList<ColumnModel>(0);

    private StreamedContent selectedFile;

    @PostConstruct
    public void init() {
        try {
            documentClassesList = documentClassRepository.findAll();
            if (documentClassesList.size() > 0) {
                selectedDocumentClassId = documentClassesList.get(0)
                    .getId();
                documentClassChanged();
            }
        } catch (Exception e) {
            log.error("Exception in init AddDocumentBean", e);
        }
    }

    public void documentClassChanged() {
        try {
            if (selectedDocumentClassId != null) {
                selectedDocumentClass = documentClassService.findWithPropertiesAndChoiceListItems(selectedDocumentClassId);
                UIUtils.generatePropertiesInputs(selectedDocumentClass.getPropertiesList(), propertiesPanelGrid, localeBean.getLocale(), Constants.OPERATION_SEARCH_DOCUMENTS, currentUserBean.getUser());
            }
        } catch (Exception e) {
            PrimeFaces.current()
                .ajax()
                .addCallbackParam("errorDialog", true);
            log.error("Exception in documentClassChanged", e);
        }
    }

    public void search() {
        try {
            if (selectedDocumentClassId == null) {
                GeneralUtils.addErrorMessage("يجب اختيار التصنيف أولا", null);
                return;
            }
            propertiesWhereStatementsList.clear();
            propertiesWhereStatementsList = UIUtils.prepareSeachProperties(propertiesPanelGrid, selectedDocumentClass.getPropertiesList());
            propertiesWhereStatementsList.add(CustomColumnsEnum.DOCUMENT_CLASS_ID.getValue() + " = " + selectedDocumentClassId);
            for (String where : propertiesWhereStatementsList) {
                System.out.println("####### where: " + where);
            }
            documentsList = utilsRepository.findDocuments(selectedDocumentClass.getTableName()
                .getValue(), selectedDocumentClass.getPropertiesList(), GeneralUtils.generateWhereStatementsString(propertiesWhereStatementsList),
                GeneralUtils.generateColumnsString(selectedDocumentClass.getPropertiesList(), Constants.OPERATION_SEARCH_DOCUMENTS), 1);
            System.out.println("########### documentsList: " + documentsList.size());
            renderSearchTable = documentsList.size() > 0;
            if (documentsList.size() > 0)
                columns = UIUtils.generateTableColumns(selectedDocumentClass.getPropertiesList());
            System.out.println("########### renderSearchTable: " + renderSearchTable);
            System.out.println("########### columns: " + columns.size());
        } catch (Exception e) {
            PrimeFaces.current()
                .ajax()
                .addCallbackParam("errorDialog", true);
            log.error("Exception in documentClassChanged", e);
        }
    }

    public void loadSelectedFile(Document document) {
        try {
            System.out.println("########## loadSelectedFile,document: " + document.getFullPath());
            System.out.println(document.getFullPath()
                .replace("\\", "\\\\"));
            InputStream is = Files.newInputStream(Paths.get(document.getFullPath()
                .replace("\\", "\\\\")));
            selectedFile = new DefaultStreamedContent(is, document.getMimeType(), document.getOriginalFileName());
        } catch (IOException e) {
            e.printStackTrace();
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

    public StreamedContent getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(StreamedContent selectedFile) {
        this.selectedFile = selectedFile;
    }

}
