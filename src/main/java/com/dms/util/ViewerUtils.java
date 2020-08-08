package com.dms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.dms.entities.Document;

public class ViewerUtils {

	public List<String> doLoadImageIntoViewer(Document document) throws Exception {

		List<String> filesList = new ArrayList<String>();
		String mimeType = document.getMimeType().toLowerCase();
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

		}
		return filesList;
	}

	private String copyFileToTemp(String inputFilePath) throws IOException {
		File file = new File(inputFilePath);
		String extension = FilenameUtils.getExtension(file.getName());
		InputStream inputStream = new FileInputStream(file);
		String newFileName = UUID.randomUUID().toString() + "." + extension;
		String pathName = GeneralUtils.getTempFolderPhysicalPath() + File.separator + newFileName;
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

}
