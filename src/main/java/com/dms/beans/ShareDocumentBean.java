package com.dms.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.dms.dao.DocumentRepository;
import com.dms.entities.Document;
import com.dms.util.GeneralUtils;

@Component("shareDocumentBean")
@Scope("view")
public class ShareDocumentBean implements Serializable {

	private static final long serialVersionUID = -5291118986433281906L;

	private static final Logger log = LoggerFactory.getLogger(ShareDocumentBean.class);

	@Autowired
	private DocumentRepository documentRepository;

	private Document document;

	@PostConstruct
	public void init() {
		System.out.println("####### ShareDocumentBean init ########");
		try {
			String shareUUID = GeneralUtils.getHttpServletRequest().getParameter("shareUUID");
			if (StringUtils.isNotBlank(shareUUID)) {
				document = documentRepository.findByShareUUID(shareUUID.trim());
			}
		} catch (Exception e) {
			log.error("Exception in init ShareDocumentBean", e);
			throw new RuntimeException(e);
		}
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

}
