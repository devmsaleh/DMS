package com.dms.enums;

public enum ColumnDatabaseTypeEnum {

	SMALL_TEXT("TEXT(200)"), LONG_TEXT("TEXT(1500)"), SMALL_NUMBER("INT(8) UNSIGNED"), LONG_NUMBER(
			"BIGINT"), NORMAL_CHOICELIST("TEXT(255)"), MAIN_CHOICELIST("TEXT(255)"), SUB_CHOICELIST(
					"TEXT(255)"), HIJRI_DATE("INT(8) UNSIGNED"), GREG_DATE("INT(8) UNSIGNED");

	private String value;

	public String getValue() {
		return value;
	}

	private ColumnDatabaseTypeEnum(String value) {
		this.value = value;
	}

}
