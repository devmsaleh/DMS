package com.dms.service;

import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dms.dao.ChoiceListItemRepository;
import com.dms.dao.ChoiceListRepository;
import com.dms.dao.DocumentClassRepository;
import com.dms.dao.DocumentRepository;
import com.dms.entities.ChoiceList;
import com.dms.entities.ChoiceListItem;
import com.dms.entities.Document;
import com.dms.entities.DocumentClass;
import com.dms.entities.Property;

@Service
@Transactional(rollbackFor = Exception.class)
public class DocumentClassService {

    private static final Logger log = LoggerFactory.getLogger(DocumentClassService.class);

    @Autowired
    private DocumentClassRepository documentClassRepository;

    @Autowired
    private ChoiceListRepository choiceListRepository;

    @Autowired
    private ChoiceListItemRepository choiceListItemRepository;

    @Autowired
    private DocumentRepository documentRepository;

    public DocumentClass findWithProperties(Long id) {
        DocumentClass documentClass = documentClassRepository.getOne(id);
        if (documentClass != null) {
            Hibernate.initialize(documentClass.getPropertiesList());
        }
        return documentClass;
    }

    public DocumentClass findWithPropertiesAndChoiceListItems(Long id) {
        DocumentClass documentClass = documentClassRepository.getOne(id);
        if (documentClass != null) {
            Hibernate.initialize(documentClass.getPropertiesList());
            for (Property property : documentClass.getPropertiesList()) {
                if (property.getChoiceListId() != null && property.getChoiceListId() > 0) {
                    List<ChoiceListItem> choiceListItemsList = choiceListItemRepository.findByChoiceListId(property.getChoiceListId());
                    if (choiceListItemsList != null) {
                        property.setChoiceListItemsList(choiceListItemsList);
                    }
                }
            }
        }
        return documentClass;
    }

    public ChoiceList findChoiceListWithItems(Long id) {
        ChoiceList choiceList = choiceListRepository.getOne(id);
        if (choiceList != null) {
            Hibernate.initialize(choiceList.getItemsList());
        }
        return choiceList;
    }

    public Document getOne(Long id) {
        return documentRepository.getOne(id);
    }

}
