package com.dms.service;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.dms.util.Constants;

@Service
public class CacheService {

	private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

	@CacheEvict(cacheNames = Constants.CACHE_NAME_COUNTRIES, allEntries = true)
	public void clearCountriesCache() {

	}

	public void refreshAllCaches() {
		DateTime startDate = DateTime.now();
		logger.info("######### refreshAllCaches started at: " + startDate);
		clearCountriesCache();
		DateTime endDate = DateTime.now();
		int seconds = Seconds.secondsBetween(startDate, endDate).getSeconds();
		logger.info("######### refreshAllCaches ended at: " + endDate + ", it took " + seconds + " seconds");
	}

}
