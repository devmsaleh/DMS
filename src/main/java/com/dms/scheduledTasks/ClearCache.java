package com.dms.scheduledTasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClearCache {

	private static final Logger logger = LoggerFactory.getLogger(ClearCache.class);

	// clear error codes cache each 1 hour
	@Scheduled(fixedRate = 3600000, initialDelay = 10000)
	public void clearErrorCodesCache() {
		try {

		} catch (Exception e) {
			logger.error("Exception in ClearCache service", e);
		}
	}

}
