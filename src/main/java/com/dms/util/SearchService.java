package com.dms.util;

import org.apache.commons.lang3.StringUtils;

public class SearchService {

	public static final String EQUAL_TO_CONDITION_KEYWORD = "=";

	/**
	 *
	 */
	public static final String NOT_EQUAL_TO_CONDITION_KEYWORD = "!=";

	/**
	 *
	 */
	public static final String GREATER_THAN_CONDITION_KEYWORD = ">";

	/**
	 *
	 */
	public static final String GREATER_THAN_OR_EQUAL_CONDITION_KEYWORD = ">=";

	/**
	 *
	 */
	public static final String LESS_THAN_CONDITION_KEYWORD = "<";

	/**
	 *
	 */
	public static final String LESS_THAN_OR_EQUAL_CONDITION_KEYWORD = "<=";

	/**
	 *
	 */
	public static final String LIKE_CONDITION_KEYWORD = "like";

	/**
	 *
	 */
	public static final String NOT_LIKE_CONDITION_KEYWORD = "not like";

	public static String constructStringWhere(String propName, String conditionOp, String propValue) {
		String where = "";

		if (conditionOp.equalsIgnoreCase(LIKE_CONDITION_KEYWORD)) {
			where = propName + " " + " LIKE '%" + propValue + "%'";
		} else if (conditionOp.equalsIgnoreCase(NOT_LIKE_CONDITION_KEYWORD)) {
			where = "NOT (" + propName + " LIKE '%" + propValue + "%')";
		} else if (conditionOp.equalsIgnoreCase(EQUAL_TO_CONDITION_KEYWORD)) {
			where = propName + " " + "=" + " '" + propValue + "'";
		} else if (conditionOp.equalsIgnoreCase(NOT_EQUAL_TO_CONDITION_KEYWORD)) {
			where = "NOT (" + propName + " = '" + propValue + "')";
		} else {
			where = propName + " " + conditionOp + " '" + propValue + "'";
		}

		return where;
	}

	/**
	 * Creates a where statement for integer property based on given condition
	 * 
	 * @param propName
	 * @param conditionOp
	 * @param propValue
	 * @return
	 */
	public static String constructIntWhere(String propName, String conditionOp, int propValue) {
		String where = "";
		if (conditionOp.equalsIgnoreCase(NOT_EQUAL_TO_CONDITION_KEYWORD)) {
			where = "NOT (" + propName + " = " + propValue + ")";
		} else {
			where = propName + " " + conditionOp + " " + Integer.toString(propValue);
		}
		return where;
	}

	/**
	 * Creates a where statement for boolean property based on given condition
	 * 
	 * @param propName
	 * @param conditionOp
	 * @param propValue
	 * @return
	 */
	public static String constructBooleanWhere(String propName, String conditionOp, boolean propValue) {
		String where = "";
		where = propName + " " + conditionOp + " " + Boolean.toString(propValue);
		return where;
	}

	public static String createPropertyWhereStatement(String propertyName, String propertyValue, String dataType,
			boolean isIntegrationSearch) {

		// ECMLogger.debugStatic("#######
		// createPropertyWhereStatement,propertyName:
		// "+propertyName+",propertyValue: "+propertyValue+",dataType:
		// "+dataType);

		String whereStatement = "";
		if (StringUtils.isBlank(propertyValue)) {
			return whereStatement;
		}

		if (StringUtils.isBlank(dataType)) {
			dataType = InputsDataTypes.STRING;
		}

		if (dataType.equalsIgnoreCase(InputsDataTypes.STRING)) {
			if (isIntegrationSearch)
				whereStatement = SearchService.constructStringWhere(propertyName,
						SearchService.EQUAL_TO_CONDITION_KEYWORD, propertyValue);
			else
				whereStatement = SearchService.constructStringWhere(propertyName, SearchService.LIKE_CONDITION_KEYWORD,
						propertyValue);
		}

		else if (dataType.equalsIgnoreCase(InputsDataTypes.INTEGER)) {
			whereStatement = SearchService.constructIntWhere(propertyName, SearchService.EQUAL_TO_CONDITION_KEYWORD,
					Integer.parseInt(propertyValue));
		}

		else if (dataType.equalsIgnoreCase(InputsDataTypes.CHOICE_LIST)
				|| dataType.equalsIgnoreCase(InputsDataTypes.LONG)) {
			whereStatement = SearchService.constructStringWhere(propertyName, SearchService.EQUAL_TO_CONDITION_KEYWORD,
					propertyValue);
		}

		else
			whereStatement = SearchService.constructStringWhere(propertyName, SearchService.LIKE_CONDITION_KEYWORD,
					propertyValue);

		return whereStatement;
	}

}
