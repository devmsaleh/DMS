package com.dms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dms.entities.DocumentClass;

public interface DocumentClassRepository extends JpaRepository<DocumentClass, Long> {

	int countByDisplayNameArabicIgnoreCase(String displayNameArabic);

	List<DocumentClass> findByDeletedFalse();

	int countBySymbolicNameIgnoreCase(String symbolicName);

}
