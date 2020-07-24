package com.dms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dms.entities.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

	@Query("select fullPath from Document where id=:id")
	String findDocumentPath(@Param("id") long id);

}
