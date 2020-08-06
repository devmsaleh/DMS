package com.dms.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("editDocumentBean")
@Scope("view")
public class EditDocumentBean implements Serializable {

	private static final long serialVersionUID = 7827864578714382953L;

	private static final Logger log = LoggerFactory.getLogger(EditDocumentBean.class);

	@PostConstruct
	public void init() {
		try {
		} catch (Exception e) {
			log.error("Exception in init editDocumentBean", e);
		}
	}

}
