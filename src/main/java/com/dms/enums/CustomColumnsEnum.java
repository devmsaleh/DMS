package com.dms.enums;

public enum CustomColumnsEnum {

	CONTENT_SIZE("content_size"), DOCUMENT_CLASS_ID("document_class_id"), CREATED_BY_ID("created_by_id"),
	DATE_CREATED("date_created"), FILE_NAME("file_name"), FULL_PATH("full_path"), MIME_TYPE("mime_type"),
	ORIGINAL_FILE_NAME("original_file_name"), ID("id"), UUID("uuid"), DATE_LAST_MODIFIED("date_last_modified"),
	LAST_MODIFIED_BY_ID("last_modified_by_id");

	private String value;

	public String getValue() {
		return value;
	}

	private CustomColumnsEnum(String value) {
		this.value = value;
	}

}
