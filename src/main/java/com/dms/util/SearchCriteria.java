package com.dms.util;

public class SearchCriteria {

	private String propertyName;
	private String criteriaLabel;
	private String propertyValue;

	public SearchCriteria() {

	}

	public SearchCriteria(String propertyName, String criteriaLabel, String propertyValue) {
		this.propertyName = propertyName;
		this.criteriaLabel = criteriaLabel;
		this.propertyValue = propertyValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getCriteriaLabel() {
		return criteriaLabel;
	}

	public void setCriteriaLabel(String criteriaLabel) {
		this.criteriaLabel = criteriaLabel;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

}
