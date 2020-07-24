package com.dms.enums;

public enum DocumentClassTableNameEnum {

	DEFAULT_TABLE("document"), TABLE1("document1");

	private String value;

	public String getValue() {
		return value;
	}

	private DocumentClassTableNameEnum(String value) {
		this.value = value;
	}

}
