package com.dms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dms.entities.ChoiceListItem;

public interface ChoiceListItemRepository extends JpaRepository<ChoiceListItem, Long> {

	List<ChoiceListItem> findByChoiceListId(Long choiceListId);

}
