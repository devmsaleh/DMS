package com.dms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dms.dao.DocumentRepository;
import com.dms.entities.Document;
import com.dms.util.GeneralUtils;
import com.dms.util.ViewerUtils;
import com.google.gson.Gson;

@Component("viewerBean")
@Scope("view")
public class ViewerBean implements Serializable {

	private static final long serialVersionUID = -9134387815711887626L;

	private static final Logger log = LoggerFactory.getLogger(ViewerBean.class);

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private CurrentUserBean currentUserBean;

	private String downloadFileName = "untitled.pdf";

	private List<String> filesList = new ArrayList<String>();

	private boolean hideForm;

	private Document document;

	public String getFilesListAsJson() {
		return new Gson().toJson(getFilesList());
	}

	@PostConstruct
	public void init() {
		System.out.println("####### viewerBean init ########");
		try {
			String uuid = GeneralUtils.getHttpServletRequest().getParameter("uuid");
			loadDocument(uuid);
		} catch (Exception e) {
			log.error("Exception in init ViewerBean", e);
			throw new RuntimeException(e);
		}
	}

	private void loadDocument(String uuid) throws Exception {

		System.out.println("###### doLoadDocument,uuid: " + uuid);
		filesList.clear();
		document = null;
		if (StringUtils.isBlank(uuid)) {
			GeneralUtils.showDialogError("معرف الوثيقة غير موجود");
			return;
		}
		document = documentRepository.findByUuid(uuid);
		System.out.println("######### document: " + document);
		if (document == null)
			return;
		System.out.println("######### document.getMimeType(): " + document.getMimeType());
		if (document.getMimeType().contains("image")) {
			filesList = new ViewerUtils().doLoadImageIntoViewer(document);
			System.out.println("######### filesList after doLoadImageIntoViewer: " + filesList);
		}
	}

	public boolean isNextEnabled() {
		int index = currentUserBean.getResultUUIDList().indexOf(document.getUuid());
		boolean enabled = (index + 1) != currentUserBean.getResultUUIDList().size();
		System.out.println("########### isNextEnabled,index: " + index + ",enabled: " + enabled);
		return enabled;
	}

	public boolean isPreviousEnabled() {
		int index = currentUserBean.getResultUUIDList().indexOf(document.getUuid());
		boolean enabled = (index - 1) > 0;
		System.out.println("########### isPreviousEnabled,index: " + index + ",enabled: " + enabled);
		return enabled;
	}

	public void next() {
		try {
			int index = currentUserBean.getResultUUIDList().indexOf(document.getUuid());
			document = null;
			if ((index + 1) != currentUserBean.getResultUUIDList().size()) {
				String nextUUID = currentUserBean.getResultUUIDList().get(index + 1);
				loadDocument(nextUUID);
			} else {
				hideForm = true;
				GeneralUtils.showDialogError("تم استعراض جميع نتائج البحث");
			}
		} catch (Exception e) {
			log.error("Exception in ViewerBean next", e);
			GeneralUtils.showSystemErrorDialog();
		}
	}

	public void previous() {
		try {
			int index = currentUserBean.getResultUUIDList().indexOf(document.getUuid());
			document = null;
			if ((index + -1) != currentUserBean.getResultUUIDList().size()) {
				String prevUUID = currentUserBean.getResultUUIDList().get(index - 1);
				loadDocument(prevUUID);
			} else {
				hideForm = true;
				GeneralUtils.showDialogError("تم استعراض جميع نتائج البحث");
			}
		} catch (Exception e) {
			log.error("Exception in ViewerBean previous", e);
			GeneralUtils.showSystemErrorDialog();
		}
	}

	public List<String> getFilesList() {
		return filesList;
	}

	public void setFilesList(List<String> filesList) {
		this.filesList = filesList;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public boolean isHideForm() {
		return hideForm;
	}

	public void setHideForm(boolean hideForm) {
		this.hideForm = hideForm;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
