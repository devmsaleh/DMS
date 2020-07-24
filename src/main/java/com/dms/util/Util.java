package com.dms.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;

public class Util {
	public boolean isStringValueEmpty(String value) {
		return value == null || value.trim().equals("") || value.trim().equalsIgnoreCase("null");
	}

	public boolean isStringsEmpty(String... values) {
		for (String value : values) {
			boolean empty = value == null || value.trim().equals("") || value.trim().equalsIgnoreCase("null");
			if (empty)
				return empty;
		}
		return false;
	}

	public boolean isOneOfObjectsNull(Object... objects) {
		for (Object object : objects) {
			boolean isNull = object == null;
			if (isNull)
				return isNull;
		}
		return false;
	}

	public boolean isValidInteger(String value) {
		return isValidRegix("\\d+", value);
	}

	public int getIntegerValue(String value, int invalidValue) {
		if (isStringValueEmpty(value))
			return invalidValue;
		return isValidInteger(value) ? Integer.parseInt(value) : invalidValue;
	}

	public long getLongValue(String value, int invalidValue) {
		if (isStringValueEmpty(value))
			return invalidValue;
		return isValidInteger(value) ? Long.parseLong(value) : invalidValue;
	}

	public int getIntegerValue(Object obj, int invalidValue) {
		String value = obj + "";
		if (isStringValueEmpty(value))
			return invalidValue;
		return isValidInteger(value) ? Integer.parseInt(value) : invalidValue;
	}

	public boolean isValidRegix(String reg, String value) {
		if (isStringValueEmpty(value) || isStringValueEmpty(reg))
			return false;
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(value);
		boolean matchFound = m.matches();
		if (!matchFound) {
			return false;
		}
		return true;

	}

	public static String LANG_ARABIC = "ar";
	public static String LANG_ENGLISH = "en";

	public String getLang(String lang) {
		if (!isStringValueEmpty(lang)) {
			lang = lang.equalsIgnoreCase(LANG_ARABIC) || lang.equalsIgnoreCase(LANG_ENGLISH) ? lang : LANG_ARABIC;
		} else
			lang = LANG_ARABIC;
		return lang;
	}

	public boolean isEmailValid(String enteredEmail) {
		// Set the email pattern string
		Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

		// Match the given string with the pattern
		Matcher m = p.matcher(enteredEmail.toLowerCase());

		// Check whether match is found
		boolean matchFound = m.matches();

		if (!matchFound) {
			return false;
		}
		return true;
	}

	public byte[] getByteArrayFromEncoded64File(String base64File) {
		try {
			if (base64File == null)
				return null;
			byte[] bytes = new BASE64Decoder().decodeBuffer(base64File);
			return bytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getInputStreamFromByteArray(byte[] decodedBytes) {
		if (decodedBytes != null)
			return new ByteArrayInputStream(decodedBytes);
		return null;
	}

	public InputStream getInputStreamFromEncoded64File(String base64File) {
		byte[] decodedBytes = getByteArrayFromEncoded64File(base64File);
		return getInputStreamFromByteArray(decodedBytes);
	}

	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-zA-Z]).{8,20})";

	/**
	 * Validate password with regular expression
	 * 
	 * @param password
	 *            password for validation
	 * @return true valid password, false invalid password
	 */
	public boolean validatePassword(final String password) {
		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public String formatCalendar(Calendar calendar) {
		if (calendar == null)
			return "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dateFormat.setCalendar(calendar);
		String dateTimeFormatted = dateFormat.format(calendar.getTime());
		return dateTimeFormatted;
	}
}
