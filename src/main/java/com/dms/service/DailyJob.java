package com.dms.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyJob {

	private static final Logger logger = LoggerFactory.getLogger(DailyJob.class);

	@Autowired
	private CacheService cacheService;

	@Scheduled(fixedRate = 3600000 * 1, initialDelay = 0)
	public void refreshCache() {
		try {
			logger.info("######## DMS Job at: " + new Date());
			cacheService.refreshAllCaches();
		} catch (Exception e) {
			logger.error("Exception in DailyJob", e);
		}
	}

}
