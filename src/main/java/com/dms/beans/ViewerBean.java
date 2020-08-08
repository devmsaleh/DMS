package com.dms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.StreamedContent;
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

	private String downloadFileName = "untitled.pdf";

	private StreamedContent content;

	private List<String> filesList = new ArrayList<String>();

	public String getFilesListAsJson() {
		return new Gson().toJson(getFilesList());
	}

	@PostConstruct
	public void init() {
		System.out.println("####### viewerBean init ########");
		try {
			String id = GeneralUtils.getHttpServletRequest().getParameter("id");
			System.out.println("###### doLoadDocument,id: " + id);
			if (StringUtils.isBlank(id)) {
				System.out.println("###### INVALID DOC ID #########");
				return;
			}
			Optional<Document> documentOptional = documentRepository.findById(Long.parseLong(id));
			Document document = documentOptional.get();
			if (document == null)
				return;
			filesList = new ViewerUtils().doLoadImageIntoViewer(document);
		} catch (Exception e) {
			log.error("Exception in init ViewerBean", e);
			throw new RuntimeException(e);
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

}
