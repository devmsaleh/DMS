package com.dms.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.component.html.HtmlPanelGrid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("searchBean")
@Scope("view")
public class SearchBean implements Serializable {

	private static final long serialVersionUID = 7827864578714382953L;

	private static final Logger log = LoggerFactory.getLogger(SearchBean.class);

	private transient HtmlPanelGrid propertiesPanelGrid = new HtmlPanelGrid();

	@PostConstruct
	public void init() {
		try {
		} catch (Exception e) {
			log.error("Exception in init SearchBean", e);
		}
	}

	public HtmlPanelGrid getPropertiesPanelGrid() {
		return propertiesPanelGrid;
	}

	public void setPropertiesPanelGrid(HtmlPanelGrid propertiesPanelGrid) {
		this.propertiesPanelGrid = propertiesPanelGrid;
	}

}
