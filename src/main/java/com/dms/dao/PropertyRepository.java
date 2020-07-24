package com.dms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dms.entities.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {

	int countByColumnNameIgnoreCase(String columnName);

	int countBySymbolicNameIgnoreCase(String symbolicName);

}
