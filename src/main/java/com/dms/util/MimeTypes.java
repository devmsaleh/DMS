package com.dms.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;

public class MimeTypes {

	public static final String MIME_APPLICATION_ANDREW_INSET = "application/andrew-inset";
	public static final String MIME_APPLICATION_JSON = "application/json";
	public static final String MIME_APPLICATION_ZIP = "application/zip";
	public static final String MIME_APPLICATION_X_GZIP = "application/x-gzip";
	public static final String MIME_APPLICATION_TGZ = "application/tgz";
	public static final String MIME_APPLICATION_POSTSCRIPT = "application/postscript";
	public static final String MIME_APPLICATION_PDF = "application/pdf";
	public static final String MIME_APPLICATION_JNLP = "application/jnlp";
	public static final String MIME_APPLICATION_MAC_BINHEX40 = "application/mac-binhex40";
	public static final String MIME_APPLICATION_MAC_COMPACTPRO = "application/mac-compactpro";
	public static final String MIME_APPLICATION_MATHML_XML = "application/mathml+xml";
	public static final String MIME_APPLICATION_OCTET_STREAM = "application/octet-stream";
	public static final String MIME_APPLICATION_ODA = "application/oda";
	public static final String MIME_APPLICATION_RDF_XML = "application/rdf+xml";
	public static final String MIME_APPLICATION_JAVA_ARCHIVE = "application/java-archive";
	public static final String MIME_APPLICATION_RDF_SMIL = "application/smil";
	public static final String MIME_APPLICATION_SRGS = "application/srgs";
	public static final String MIME_APPLICATION_SRGS_XML = "application/srgs+xml";
	public static final String MIME_APPLICATION_VND_MIF = "application/vnd.mif";

	public static final String MIME_APPLICATION_VND_MSEXCEL = "application/vnd.ms-excel";
	public static final String MIME_APPLICATION_VND_MSEXCEL_NEW = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String MIME_APPLICATION_VND_MSPOWERPOINT = "application/vnd.ms-powerpoint";
	public static final String MIME_APPLICATION_VND_MSPOWERPOINT_NEW = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	public static final String MIME_APPLICATION_MSWORD = "application/msword";
	public static final String MIME_APPLICATION_MSWORD_NEW = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

	public static final String MIME_APPLICATION_VND_RNREALMEDIA = "application/vnd.rn-realmedia";
	public static final String MIME_APPLICATION_X_BCPIO = "application/x-bcpio";
	public static final String MIME_APPLICATION_X_CDLINK = "application/x-cdlink";
	public static final String MIME_APPLICATION_X_CHESS_PGN = "application/x-chess-pgn";
	public static final String MIME_APPLICATION_X_CPIO = "application/x-cpio";
	public static final String MIME_APPLICATION_X_CSH = "application/x-csh";
	public static final String MIME_APPLICATION_X_DIRECTOR = "application/x-director";
	public static final String MIME_APPLICATION_X_DVI = "application/x-dvi";
	public static final String MIME_APPLICATION_X_FUTURESPLASH = "application/x-futuresplash";
	public static final String MIME_APPLICATION_X_GTAR = "application/x-gtar";
	public static final String MIME_APPLICATION_X_HDF = "application/x-hdf";
	public static final String MIME_APPLICATION_X_JAVASCRIPT = "application/x-javascript";
	public static final String MIME_APPLICATION_X_KOAN = "application/x-koan";
	public static final String MIME_APPLICATION_X_LATEX = "application/x-latex";
	public static final String MIME_APPLICATION_X_NETCDF = "application/x-netcdf";
	public static final String MIME_APPLICATION_X_OGG = "application/x-ogg";
	public static final String MIME_APPLICATION_X_SH = "application/x-sh";
	public static final String MIME_APPLICATION_X_SHAR = "application/x-shar";
	public static final String MIME_APPLICATION_X_SHOCKWAVE_FLASH = "application/x-shockwave-flash";
	public static final String MIME_APPLICATION_X_STUFFIT = "application/x-stuffit";
	public static final String MIME_APPLICATION_X_SV4CPIO = "application/x-sv4cpio";
	public static final String MIME_APPLICATION_X_SV4CRC = "application/x-sv4crc";
	public static final String MIME_APPLICATION_X_TAR = "application/x-tar";
	public static final String MIME_APPLICATION_X_RAR_COMPRESSED = "application/x-rar-compressed";
	public static final String MIME_APPLICATION_X_TCL = "application/x-tcl";
	public static final String MIME_APPLICATION_X_TEX = "application/x-tex";
	public static final String MIME_APPLICATION_X_TEXINFO = "application/x-texinfo";
	public static final String MIME_APPLICATION_X_TROFF = "application/x-troff";
	public static final String MIME_APPLICATION_X_TROFF_MAN = "application/x-troff-man";
	public static final String MIME_APPLICATION_X_TROFF_ME = "application/x-troff-me";
	public static final String MIME_APPLICATION_X_TROFF_MS = "application/x-troff-ms";
	public static final String MIME_APPLICATION_X_USTAR = "application/x-ustar";
	public static final String MIME_APPLICATION_X_WAIS_SOURCE = "application/x-wais-source";
	public static final String MIME_APPLICATION_VND_MOZZILLA_XUL_XML = "application/vnd.mozilla.xul+xml";
	public static final String MIME_APPLICATION_XHTML_XML = "application/xhtml+xml";
	public static final String MIME_APPLICATION_XSLT_XML = "application/xslt+xml";
	public static final String MIME_APPLICATION_XML = "application/xml";
	public static final String MIME_APPLICATION_TEXT_XML = "text/xml";
	public static final String MIME_APPLICATION_XML_DTD = "application/xml-dtd";
	// >>>>>>>> images mime types <<<<<<<<<<<<<< //
	public static final String MIME_IMAGE_BMP = "image/bmp";
	public static final String MIME_IMAGE_CGM = "image/cgm";
	public static final String MIME_IMAGE_GIF = "image/gif";
	public static final String MIME_IMAGE_IEF = "image/ief";
	public static final String MIME_IMAGE_JPEG = "image/jpeg";
	public static final String MIME_IMAGE_PJPEG = "image/pjpeg";
	public static final String MIME_IMAGE_TIFF = "image/tiff";
	public static final String MIME_IMAGE_PNG = "image/png";
	public static final String MIME_IMAGE_X_PNG = "image/x-png";
	public static final String MIME_IMAGE_SVG_XML = "image/svg+xml";
	public static final String MIME_IMAGE_VND_DJVU = "image/vnd.djvu";
	public static final String MIME_IMAGE_WAP_WBMP = "image/vnd.wap.wbmp";
	public static final String MIME_IMAGE_X_CMU_RASTER = "image/x-cmu-raster";
	public static final String MIME_IMAGE_X_ICON = "image/x-icon";
	public static final String MIME_IMAGE_X_PORTABLE_ANYMAP = "image/x-portable-anymap";
	public static final String MIME_IMAGE_X_PORTABLE_BITMAP = "image/x-portable-bitmap";
	public static final String MIME_IMAGE_X_PORTABLE_GRAYMAP = "image/x-portable-graymap";
	public static final String MIME_IMAGE_X_PORTABLE_PIXMAP = "image/x-portable-pixmap";
	public static final String MIME_IMAGE_X_RGB = "image/x-rgb";
	// >>>>>>>> images mime types <<<<<<<<<<<<<< //
	public static final String MIME_AUDIO_BASIC = "audio/basic";
	public static final String MIME_AUDIO_MIDI = "audio/midi";
	public static final String MIME_AUDIO_MPEG = "audio/mpeg";
	public static final String MIME_AUDIO_X_AIFF = "audio/x-aiff";
	public static final String MIME_AUDIO_X_MPEGURL = "audio/x-mpegurl";
	public static final String MIME_AUDIO_X_PN_REALAUDIO = "audio/x-pn-realaudio";
	public static final String MIME_AUDIO_X_WAV = "audio/x-wav";
	public static final String MIME_CHEMICAL_X_PDB = "chemical/x-pdb";
	public static final String MIME_CHEMICAL_X_XYZ = "chemical/x-xyz";
	public static final String MIME_MODEL_IGES = "model/iges";
	public static final String MIME_MODEL_MESH = "model/mesh";
	public static final String MIME_MODEL_VRLM = "model/vrml";
	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String MIME_TEXT_RICHTEXT = "text/richtext";
	public static final String MIME_TEXT_RTF = "text/rtf";
	public static final String MIME_TEXT_HTML = "text/html";
	public static final String MIME_TEXT_CALENDAR = "text/calendar";
	public static final String MIME_TEXT_CSS = "text/css";
	public static final String MIME_TEXT_SGML = "text/sgml";
	public static final String MIME_TEXT_TAB_SEPARATED_VALUES = "text/tab-separated-values";
	public static final String MIME_TEXT_VND_WAP_XML = "text/vnd.wap.wml";
	public static final String MIME_TEXT_VND_WAP_WMLSCRIPT = "text/vnd.wap.wmlscript";
	public static final String MIME_TEXT_X_SETEXT = "text/x-setext";
	public static final String MIME_TEXT_X_COMPONENT = "text/x-component";
	public static final String MIME_VIDEO_QUICKTIME = "video/quicktime";
	public static final String MIME_VIDEO_MPEG = "video/mpeg";
	public static final String MIME_VIDEO_VND_MPEGURL = "video/vnd.mpegurl";
	public static final String MIME_VIDEO_X_MSVIDEO = "video/x-msvideo";
	public static final String MIME_VIDEO_X_MS_WMV = "video/x-ms-wmv";
	public static final String MIME_VIDEO_X_SGI_MOVIE = "video/x-sgi-movie";
	public static final String MIME_X_CONFERENCE_X_COOLTALK = "x-conference/x-cooltalk";
	public static final String MIME_APPLICATION_OPEN_OFFICE_TEXT = "application/vnd.oasis.opendocument.text";
	public static final String MIME_APPLICATION_OPEN_OFFICE_SPREADSHEET = "application/vnd.oasis.opendocument.spreadsheet";
	public static final String MIME_APPLICATION_EXE = "application/x-msdownload";
	public static final String MIME_APPLICATION_MSC = "application/x-tika-msoffice";
	public static final String MIME_APPLICATION_WINDOWS_INSTALLER = "application/x-ms-installer";
	public static final String MIME_APPLICATION_UNIX_EXECUTABLE = "application/x-elf";
	public static final String MIME_APPLICATION_UNIX_SCRIPT = "application/x-sh";
	public static final String MIME_APPLICATION_UNIX_PERL = "text/x-perl";
	public static final String MIME_APPLICATION_UNIX_PYTHON = "text/x-python";

	private static HashMap<String, String> mimeTypeMapping;
	private static List<String> textMimeTypesList;
	private static List<String> portalMimeTypesList;
	private static List<String> executableMimeTypesList;
	private static List<String> imageMimeTypesList;

	static {

		textMimeTypesList = new ArrayList<String>(20);
		textMimeTypesList.add(MIME_APPLICATION_TEXT_XML);
		textMimeTypesList.add(MIME_TEXT_PLAIN);
		textMimeTypesList.add(MIME_TEXT_RTF);
		textMimeTypesList.add(MIME_TEXT_RICHTEXT);
		textMimeTypesList.add(MIME_APPLICATION_MSWORD);
		textMimeTypesList.add(MIME_APPLICATION_MSWORD_NEW);
		textMimeTypesList.add(MIME_APPLICATION_VND_MSEXCEL);
		textMimeTypesList.add(MIME_APPLICATION_VND_MSEXCEL_NEW);
		textMimeTypesList.add(MIME_APPLICATION_VND_MSPOWERPOINT);
		textMimeTypesList.add(MIME_APPLICATION_VND_MSPOWERPOINT_NEW);
		textMimeTypesList.add(MIME_APPLICATION_PDF);
		textMimeTypesList.add(MIME_APPLICATION_OPEN_OFFICE_TEXT);
		textMimeTypesList.add(MIME_APPLICATION_OPEN_OFFICE_SPREADSHEET);

		portalMimeTypesList = new ArrayList<String>(10);
		portalMimeTypesList.add(MIME_APPLICATION_MSWORD);
		portalMimeTypesList.add(MIME_APPLICATION_MSWORD_NEW);
		portalMimeTypesList.add(MIME_APPLICATION_PDF);
		portalMimeTypesList.add(MIME_IMAGE_GIF);
		portalMimeTypesList.add(MIME_IMAGE_JPEG);
		portalMimeTypesList.add(MIME_IMAGE_PNG);
		portalMimeTypesList.add(MIME_IMAGE_TIFF);

		imageMimeTypesList = new ArrayList<String>(20);
		imageMimeTypesList.add(MIME_IMAGE_BMP);
		imageMimeTypesList.add(MIME_IMAGE_CGM);
		imageMimeTypesList.add(MIME_IMAGE_GIF);
		imageMimeTypesList.add(MIME_IMAGE_IEF);
		imageMimeTypesList.add(MIME_IMAGE_JPEG);
		imageMimeTypesList.add(MIME_IMAGE_PJPEG);
		imageMimeTypesList.add(MIME_IMAGE_PNG);
		imageMimeTypesList.add(MIME_IMAGE_SVG_XML);
		imageMimeTypesList.add(MIME_IMAGE_TIFF);
		imageMimeTypesList.add(MIME_IMAGE_VND_DJVU);
		imageMimeTypesList.add(MIME_IMAGE_WAP_WBMP);
		imageMimeTypesList.add(MIME_IMAGE_X_CMU_RASTER);
		imageMimeTypesList.add(MIME_IMAGE_X_ICON);
		imageMimeTypesList.add(MIME_IMAGE_X_PNG);
		imageMimeTypesList.add(MIME_IMAGE_X_PORTABLE_ANYMAP);
		imageMimeTypesList.add(MIME_IMAGE_X_PORTABLE_BITMAP);
		imageMimeTypesList.add(MIME_IMAGE_X_PORTABLE_GRAYMAP);
		imageMimeTypesList.add(MIME_IMAGE_X_PORTABLE_PIXMAP);
		imageMimeTypesList.add(MIME_IMAGE_X_RGB);

		executableMimeTypesList = new ArrayList<String>(10);
		executableMimeTypesList.add(MIME_APPLICATION_EXE);
		executableMimeTypesList.add(MIME_APPLICATION_MSC);
		executableMimeTypesList.add(MIME_APPLICATION_WINDOWS_INSTALLER);
		executableMimeTypesList.add(MIME_APPLICATION_UNIX_EXECUTABLE);
		executableMimeTypesList.add(MIME_APPLICATION_UNIX_PERL);
		executableMimeTypesList.add(MIME_APPLICATION_UNIX_PYTHON);
		executableMimeTypesList.add(MIME_APPLICATION_UNIX_SCRIPT);

		mimeTypeMapping = new HashMap<String, String>(200) {
			private void put1(String key, String value) {
				if (put(key, value) != null) {
					throw new IllegalArgumentException("Duplicated extension: " + key);
				}
			}

			{
				put1("xul", MIME_APPLICATION_VND_MOZZILLA_XUL_XML);
				put1("json", MIME_APPLICATION_JSON);
				put1("ice", MIME_X_CONFERENCE_X_COOLTALK);
				put1("movie", MIME_VIDEO_X_SGI_MOVIE);
				put1("avi", MIME_VIDEO_X_MSVIDEO);
				put1("wmv", MIME_VIDEO_X_MS_WMV);
				put1("m4u", MIME_VIDEO_VND_MPEGURL);
				put1("htc", MIME_TEXT_X_COMPONENT);
				put1("etx", MIME_TEXT_X_SETEXT);
				put1("wmls", MIME_TEXT_VND_WAP_WMLSCRIPT);
				put1("wml", MIME_TEXT_VND_WAP_XML);
				put1("tsv", MIME_TEXT_TAB_SEPARATED_VALUES);
				put1("sgm", MIME_TEXT_SGML);
				put1("css", MIME_TEXT_CSS);
				put1("ifb", MIME_TEXT_CALENDAR);
				put1("wrl", MIME_MODEL_VRLM);
				put1("mesh", MIME_MODEL_MESH);
				put1("igs", MIME_MODEL_IGES);
				put1("rgb", MIME_IMAGE_X_RGB);
				put1("ppm", MIME_IMAGE_X_PORTABLE_PIXMAP);
				put1("pgm", MIME_IMAGE_X_PORTABLE_GRAYMAP);
				put1("pbm", MIME_IMAGE_X_PORTABLE_BITMAP);
				put1("pnm", MIME_IMAGE_X_PORTABLE_ANYMAP);
				put1("ico", MIME_IMAGE_X_ICON);
				put1("ras", MIME_IMAGE_X_CMU_RASTER);
				put1("wbmp", MIME_IMAGE_WAP_WBMP);
				put1("djv", MIME_IMAGE_VND_DJVU);
				put1("svg", MIME_IMAGE_SVG_XML);
				put1("ief", MIME_IMAGE_IEF);
				put1("cgm", MIME_IMAGE_CGM);
				put1("bmp", MIME_IMAGE_BMP);
				put1("xyz", MIME_CHEMICAL_X_XYZ);
				put1("pdb", MIME_CHEMICAL_X_PDB);
				put1("ram", MIME_AUDIO_X_PN_REALAUDIO);
				put1("m3u", MIME_AUDIO_X_MPEGURL);
				put1("aif", MIME_AUDIO_X_AIFF);
				put1("mp3", MIME_AUDIO_MPEG);
				put1("mid", MIME_AUDIO_MIDI);
				put1("dtd", MIME_APPLICATION_XML_DTD);
				put1("xml", MIME_APPLICATION_XML);
				put1("xslt", MIME_APPLICATION_XSLT_XML);
				put1("xhtml", MIME_APPLICATION_XHTML_XML);
				put1("src", MIME_APPLICATION_X_WAIS_SOURCE);
				put1("ustar", MIME_APPLICATION_X_USTAR);
				put1("ms", MIME_APPLICATION_X_TROFF_MS);
				put1("me", MIME_APPLICATION_X_TROFF_ME);
				put1("man", MIME_APPLICATION_X_TROFF_MAN);
				put1("roff", MIME_APPLICATION_X_TROFF);
				put1("texinfo", MIME_APPLICATION_X_TEXINFO);
				put1("tex", MIME_APPLICATION_X_TEX);
				put1("tcl", MIME_APPLICATION_X_TCL);
				put1("sv4crc", MIME_APPLICATION_X_SV4CRC);
				put1("sv4cpio", MIME_APPLICATION_X_SV4CPIO);
				put1("sit", MIME_APPLICATION_X_STUFFIT);
				put1("swf", MIME_APPLICATION_X_SHOCKWAVE_FLASH);
				put1("shar", MIME_APPLICATION_X_SHAR);
				put1("sh", MIME_APPLICATION_X_SH);
				put1("cdf", MIME_APPLICATION_X_NETCDF);
				put1("latex", MIME_APPLICATION_X_LATEX);
				put1("skm", MIME_APPLICATION_X_KOAN);
				put1("js", MIME_APPLICATION_X_JAVASCRIPT);
				put1("hdf", MIME_APPLICATION_X_HDF);
				put1("gtar", MIME_APPLICATION_X_GTAR);
				put1("spl", MIME_APPLICATION_X_FUTURESPLASH);
				put1("dvi", MIME_APPLICATION_X_DVI);
				put1("dxr", MIME_APPLICATION_X_DIRECTOR);
				put1("csh", MIME_APPLICATION_X_CSH);
				put1("cpio", MIME_APPLICATION_X_CPIO);
				put1("pgn", MIME_APPLICATION_X_CHESS_PGN);
				put1("vcd", MIME_APPLICATION_X_CDLINK);
				put1("bcpio", MIME_APPLICATION_X_BCPIO);
				put1("rm", MIME_APPLICATION_VND_RNREALMEDIA);
				put1("mif", MIME_APPLICATION_VND_MIF);
				put1("grxml", MIME_APPLICATION_SRGS_XML);
				put1("gram", MIME_APPLICATION_SRGS);
				put1("smil", MIME_APPLICATION_RDF_SMIL);
				put1("rdf", MIME_APPLICATION_RDF_XML);
				put1("ogg", MIME_APPLICATION_X_OGG);
				put1("oda", MIME_APPLICATION_ODA);
				put1("bin", MIME_APPLICATION_OCTET_STREAM);
				put1("mathml", MIME_APPLICATION_MATHML_XML);
				put1("cpt", MIME_APPLICATION_MAC_COMPACTPRO);
				put1("hqx", MIME_APPLICATION_MAC_BINHEX40);
				put1("jnlp", MIME_APPLICATION_JNLP);
				put1("ez", MIME_APPLICATION_ANDREW_INSET);
				put1("txt", MIME_TEXT_PLAIN);
				put1("rtf", MIME_TEXT_RTF);
				put1("rtx", MIME_TEXT_RICHTEXT);
				put1("html", MIME_TEXT_HTML);
				put1("zip", MIME_APPLICATION_ZIP);
				put1("rar", MIME_APPLICATION_X_RAR_COMPRESSED);
				put1("gzip", MIME_APPLICATION_X_GZIP);
				put1("tgz", MIME_APPLICATION_TGZ);
				put1("tar", MIME_APPLICATION_X_TAR);
				put1("gif", MIME_IMAGE_GIF);
				put1("jpg", MIME_IMAGE_JPEG);
				put1("tif", MIME_IMAGE_TIFF);
				put1("png", MIME_IMAGE_PNG);
				put1("au", MIME_AUDIO_BASIC);
				put1("wav", MIME_AUDIO_X_WAV);
				put1("mov", MIME_VIDEO_QUICKTIME);
				put1("mpeg", MIME_VIDEO_MPEG);
				put1("doc", MIME_APPLICATION_MSWORD);
				put1("docx", MIME_APPLICATION_MSWORD_NEW);
				put1("xls", MIME_APPLICATION_VND_MSEXCEL);
				put1("xlsx", MIME_APPLICATION_VND_MSEXCEL_NEW);
				put1("ppt", MIME_APPLICATION_VND_MSPOWERPOINT);
				put1("pptx", MIME_APPLICATION_VND_MSPOWERPOINT_NEW);
				put1("ps", MIME_APPLICATION_POSTSCRIPT);
				put1("pdf", MIME_APPLICATION_PDF);
				put1("exe", MIME_APPLICATION_EXE);
				put1("msc", MIME_APPLICATION_MSC);
				put1("jar", MIME_APPLICATION_JAVA_ARCHIVE);
				put1("odt", MIME_APPLICATION_OPEN_OFFICE_TEXT);
				put1("ods", MIME_APPLICATION_OPEN_OFFICE_SPREADSHEET);

			}
		};
	}

	public static void main(String[] args) {
		System.out.println(mimeTypeMapping.get("gif"));
	}

	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String getExtension(String mimeType) {
		if (mimeType.equalsIgnoreCase(MIME_IMAGE_PJPEG))
			mimeType = MIME_IMAGE_JPEG;
		else if (mimeType.equalsIgnoreCase(MIME_IMAGE_X_PNG))
			mimeType = MIME_IMAGE_PNG;
		else if (mimeType.equalsIgnoreCase(MIME_APPLICATION_TEXT_XML))
			mimeType = MIME_APPLICATION_XML;
		return getKeyByValue(mimeTypeMapping, mimeType);
	}

	public static String getMimeType(String ext) {
		String mimeType = mimeTypeMapping.get(ext.toLowerCase());
		if (mimeType == null) {
			mimeType = MIME_APPLICATION_OCTET_STREAM;
		}
		return mimeType;
	}

	public static boolean isTextFileMimeType(String mimeType) {
		for (String textType : textMimeTypesList) {
			if (textType.equalsIgnoreCase(mimeType))
				return true;
		}
		return false;
	}

	public static boolean isImageFileMimeType(String mimeType) {
		for (String textType : imageMimeTypesList) {
			if (textType.equalsIgnoreCase(mimeType))
				return true;
		}
		return false;
	}

	public static String getMimeTypeFromInputStream(InputStream inputStream, String fileName) throws Exception {
		String mimeType = null;
		TikaInputStream stream = null;
		try {
			TikaConfig config = TikaConfig.getDefaultConfig();
			Detector detector = config.getDetector();
			stream = TikaInputStream.get(inputStream);
			Metadata metadata = new Metadata();
			metadata.add(Metadata.RESOURCE_NAME_KEY, fileName);
			MediaType mediaType = detector.detect(stream, metadata);
			mimeType = mediaType.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				inputStream.close();
			if (stream != null)
				stream.close();
		}
		return mimeType;
	}

}
