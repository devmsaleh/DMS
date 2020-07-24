package com.dms.enums;

public enum ColumnJavaTypeEnum {

	SMALL_TEXT("String"), LONG_TEXT("String"), SMALL_NUMBER("int"), LONG_NUMBER("long"), NORMAL_CHOICELIST(
			"String"), MAIN_CHOICELIST("String"), SUB_CHOICELIST("String"), HIJRI_DATE("int"), GREG_DATE("int");

	private String value;

	public String getValue() {
		return value;
	}

	private ColumnJavaTypeEnum(String value) {
		this.value = value;
	}

}
