package com.dms.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dms.entities.ChoiceList;

public interface ChoiceListRepository extends JpaRepository<ChoiceList, Long> {

	int countByDisplayNameArabicIgnoreCase(String displayNameArabic);

}
