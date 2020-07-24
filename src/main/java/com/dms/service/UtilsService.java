package com.dms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UtilsService {

	private static final Logger log = LoggerFactory.getLogger(UtilsService.class);

}
