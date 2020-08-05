package com.dms.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dms.dao.DocumentRepository;
import com.dms.entities.Document;
import com.dms.util.GeneralUtils;
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

	@PostConstruct
	public void init() {
		System.out.println("####### viewerBean init ########");
		try {
			String mimeType = GeneralUtils.getHttpServletRequest().getParameter("t");
			if (StringUtils.isBlank(mimeType) || mimeType.toLowerCase().contains("image"))
				doLoadDocument();
		} catch (Exception e) {
			log.error("Exception in init ViewerBean", e);
			throw new RuntimeException(e);
		}
	}

	private void doLoadDocument() throws Exception {
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
		String mimeType = document.getMimeType().toLowerCase();
		System.out.println("######### mimeType: " + mimeType + ",fileName: " + document.getFileName());
		if (mimeType.contains("image")) {

			if (mimeType.equalsIgnoreCase("image/tiff")) {
				filesList = GeneralUtils.convertTiffToPNG(document.getFullPath());
			} else {
				filesList.add(document.getFullPath());
			}
			List<String> finalList = new ArrayList<String>();
			for (String str : filesList) {
				String newFile = copyFileToTemp(str);
				finalList.add(newFile);
			}
			filesList.clear();
			filesList.addAll(finalList);

		} else if (mimeType.contains("pdf")) {
			downloadFileName = document.getFileName();
			FileInputStream fis = new FileInputStream(new File(document.getFullPath()));
			content = DefaultStreamedContent.builder().contentType(mimeType).name(document.getFileName())
					.stream(() -> fis).build();
		}
	}

	private String copyFileToTemp(String inputFilePath) throws IOException {
		File file = new File(inputFilePath);
		String extension = FilenameUtils.getExtension(file.getName());
		System.out.println("inputFilePath: " + inputFilePath);
		System.out.println("extension: " + extension);
		InputStream inputStream = new FileInputStream(file);
		String newFileName = UUID.randomUUID().toString() + "." + extension;
		System.out.println("newFileName: " + newFileName);
		String pathName = GeneralUtils.getTempFolderPhysicalPath() + File.separator + newFileName;
		System.out.println("pathName: " + pathName);
		File newFile = new File(pathName);
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(newFile);
			IOUtils.copy(inputStream, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
		}

		return "/dms/temp/" + newFileName;

	}

	public String getFilesListAsJson() {
		return new Gson().toJson(getFilesList());
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

	public StreamedContent getContent() {
		if (content == null) {
			System.out.println("######### getContent #####");
			try {
				doLoadDocument();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	public void setContent(StreamedContent content) {
		this.content = content;
	}

}
