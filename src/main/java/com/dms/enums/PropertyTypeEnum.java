package com.dms.enums;

public enum PropertyTypeEnum {

	SMALL_TEXT("SMALL_TEXT", "TEXT", "String"), LONG_TEXT("LONG_TEXT", "TEXT", "String"),
	MULTI_TEXT("MULTI_TEXT", "TEXT", "String"), SMALL_NUMBER("SMALL_NUMBER", "INT(8) UNSIGNED", "int"),
	LONG_NUMBER("LONG_NUMBER", "BIGINT", "long"), NORMAL_CHOICELIST("NORMAL_CHOICELIST", "TEXT", "String"),
	MAIN_CHOICELIST("MAIN_CHOICELIST", "TEXT", "String"), SUB_CHOICELIST("SUB_CHOICELIST", "TEXT", "String"),
	HIJRI_DATE("HIJRI_DATE", "INT(8) UNSIGNED", "int"), GREG_DATE("GREG_DATE", "INT(8) UNSIGNED", "int");

	private String value;
	private String databaseType;
	private String javaType;

	public String getValue() {
		return value;
	}

	private PropertyTypeEnum(String value, String databaseType, String javaType) {
		this.value = value;
		this.databaseType = databaseType;
		this.javaType = javaType;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public String getJavaType() {
		return javaType;
	}

}
