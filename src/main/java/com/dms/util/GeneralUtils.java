package com.dms.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;

import com.dms.entities.Property;
import com.dms.enums.CustomColumnsEnum;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

public class GeneralUtils {

	public static void main(String[] args) throws Exception {
		String filePath = "C:\\Users\\mahsaleh\\Desktop\\multi.tif";
		convertTiffToPNG(filePath);
	}

	public static List<String> convertTiffToPNG(String filePath) {
		List<String> filesList = new ArrayList<String>();
		try {
			File file = new File(filePath);
			try (InputStream is = new FileInputStream(file)) {
				try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(is)) {
					Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
					if (iterator == null || !iterator.hasNext()) {
						throw new RuntimeException(
								"Image file format not supported by ImageIO: " + file.getAbsolutePath());
					}

					// We are just looking for the first reader compatible:
					ImageReader reader = iterator.next();
					reader.setInput(imageInputStream);

					int numPage = reader.getNumImages(true);

					// it uses to put new png files, close to original example n0_.tiff will be in
					// /png/n0_0.png
					String name = FilenameUtils.getBaseName(file.getAbsolutePath());
					// String parentFolder = file.getParentFile().getAbsolutePath();
					String parentFolder = getTempFolderPhysicalPath();

					IntStream.range(0, numPage).forEach(v -> {
						try {
							final BufferedImage tiff = reader.read(v);
							String splittedFileName = UUID.randomUUID().toString() + ".png";
							String splitPath = parentFolder + "/" + splittedFileName;
							ImageIO.write(tiff, "png", new File(splitPath));
							String contextPath = FacesContext.getCurrentInstance().getExternalContext()
									.getRequestContextPath();
							System.out.println("########## contextPath: " + contextPath);
							String splittedFileNameFinal = contextPath + "/" + Constants.TEMP_IMAGES_FOLDER + "/"
									+ splittedFileName;
							System.out.println("########## splittedFileNameFinal: " + splittedFileNameFinal);
							filesList.add(splittedFileNameFinal);
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filesList;
	}

	private static void convertTiffToPNG1(String filePath) throws IOException, DocumentException {
		Image image;
		RandomAccessSource ras = new RandomAccessSourceFactory().createSource(new FileInputStream(filePath));
		RandomAccessFileOrArray ra = new RandomAccessFileOrArray(ras);
		int pages = TiffImage.getNumberOfPages(ra);
		for (int i = 1; i <= pages; i++) {
			image = TiffImage.getTiffImage(ra, i);
			// System.out.println("####### data: " + image.getRawData());
			// Path path = Paths.get("C:\\Users\\DELL\\Desktop\\splitted_" + (i) + ".tif");
			// Files.write(path, image.getRawData());

			BufferedImage tif = ImageIO.read(new ByteArrayInputStream(image.getRawData()));
			ImageIO.write(tif, "png", new File("C:\\Users\\mahsaleh\\Desktop\\splitted_" + (i) + ".png"));
		}

	}

	public static void splitTiff(String imagePath) throws Exception {
		ImageInputStream is = ImageIO.createImageInputStream(new File(imagePath));
		if (is == null || is.length() == 0) {
			// handle error
		}
		Iterator<ImageReader> iterator = ImageIO.getImageReaders(is);
		if (iterator == null || !iterator.hasNext()) {
			throw new IOException("Image file format not supported by ImageIO: " + imagePath);
		}
		// We are just looking for the first reader compatible:
		ImageReader reader = (ImageReader) iterator.next();
		iterator = null;
		reader.setInput(is);
		System.out.println(reader.getNumImages(true));
	}

	public static final String DATE_FORMAT = "a hh:mm yyyy-MM-dd";

	public static String formateDate(Date date) {
		if (date == null)
			return "";
		try {
			return new SimpleDateFormat(DATE_FORMAT).format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return date.toString();
		}
	}

	public static HttpServletRequest getHttpServletRequest() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request;
	}

	public static HttpSession getHttpSession() {
		FacesContext fCtx = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
		return session;
	}

	public static String getTempFolderPhysicalPath() {
		ServletContext servletContext = GeneralUtils.getHttpSession().getServletContext();
		String applicationPhyisicalPath = servletContext.getRealPath("/");
		String tempSessionFolder = applicationPhyisicalPath + Constants.TEMP_IMAGES_FOLDER;
		return tempSessionFolder;
	}

	public static void addInfoMessage(String message, String id) {
		addMessage(message, id, FacesMessage.SEVERITY_INFO);
	}

	public static void addErrorMessage(String message, String id) {
		addMessage(message, id, FacesMessage.SEVERITY_ERROR);
	}

	public static void addMessage(String message, String id, Severity severity) {
		String formId = "form";
		String clientId = null;
		if (StringUtils.isNotBlank(id)) {
			clientId = formId + ":" + id;
		}
		FacesContext.getCurrentInstance().validationFailed();
		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(severity, message, message));
		if (StringUtils.isNotBlank(id)) {
			PrimeFaces.current().scrollTo(clientId);
		}
	}

	public static void redirect(String url) {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		String redirectUrl = ec.getRequestContextPath() + url;
		try {
			ec.redirect(redirectUrl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDefaultAvatarPath() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
				.getContext();
		return servletContext.getRealPath("/resources/images/defaultAvatar.png");
	}

	public static String getCurrentTimeMYSQL() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String generateColumnsStringForAdd(List<Property> properties) {
		StringBuffer columns = new StringBuffer();

		for (int i = 0; i < properties.size(); i++) {
			columns.append(properties.get(i).getColumnName());
			if (i < properties.size() - 1) {
				columns.append(",");
			}
		}

		return columns.toString();
	}

	public static String generateColumnsStringForEdit(List<Property> properties) {
		StringBuffer columns = new StringBuffer();
		columns.append("set ");
		for (int i = 0; i < properties.size(); i++) {
			columns.append(properties.get(i).getColumnName()).append("=?");
			if (i < properties.size() - 1) {
				columns.append(",");
			}
		}

		return columns.toString();
	}

	public static String generateColumnsStringForSearch(List<Property> properties) {
		StringBuffer columns = new StringBuffer();

		columns.append(CustomColumnsEnum.FULL_PATH.getValue());
		columns.append(",").append(CustomColumnsEnum.CONTENT_SIZE.getValue());
		columns.append(",").append(CustomColumnsEnum.ORIGINAL_FILE_NAME.getValue());
		columns.append(",").append(CustomColumnsEnum.MIME_TYPE.getValue());
		columns.append(",").append(CustomColumnsEnum.FILE_NAME.getValue());
		columns.append(",").append(CustomColumnsEnum.ID.getValue());
		columns.append(",").append(CustomColumnsEnum.DATE_CREATED.getValue());
		columns.append(",").append(CustomColumnsEnum.UUID.getValue());
		columns.append(",");

		for (int i = 0; i < properties.size(); i++) {
			columns.append(properties.get(i).getColumnName());
			if (i < properties.size() - 1) {
				columns.append(",");
			}
		}

		return columns.toString();
	}

	public static String generateWhereStatementsString(List<String> whereStatements) {
		StringBuffer whereStr = new StringBuffer();
		for (String where : whereStatements) {
			whereStr.append(" AND ").append(where);
		}
		return whereStr.toString();
	}

	public static String formatIntDate(int date) {
		String dateStr = String.valueOf(date);
		if (dateStr.length() < 8)
			return dateStr;
		dateStr = dateStr.substring(0, 4) + "/" + dateStr.substring(4, 6) + "/" + dateStr.substring(6, 8);
		return dateStr;
	}

	public static String getBase64(String filePath) throws IOException {
		return DatatypeConverter.printBase64Binary(Files.readAllBytes(Paths.get(filePath)));
	}

	public static void showSystemErrorDialog() {
		String message = "حدث خطأ فى النظام...يرجى المحاولة مرة أخرى بعد قليل...واذا استمرت المشكلة يرجى الاتصال بالدعم الفني";
		FacesContext.getCurrentInstance().validationFailed();
		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_FATAL, "خطأ", message));
	}

	public static void showDialogError(String message) {
		PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_FATAL, "خطأ", message));
	}

	public static void showDialogInfo(String message) {
		PrimeFaces.current().dialog()
				.showMessageDynamic(new FacesMessage(FacesMessage.SEVERITY_INFO, "تنبيه", message));
	}

}
