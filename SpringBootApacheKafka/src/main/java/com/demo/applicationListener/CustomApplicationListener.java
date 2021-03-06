package com.demo.applicationListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
/**
 * Application listener for server startup completion. 
 * @author Vinayak Manjare
 *
 */
@Component
@Order(0)
public class CustomApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
	private final Logger logger = LoggerFactory.getLogger(ApplicationListener.class);
	@Autowired
	com.demo.controller.ServerDetailsHelper serverDetailsHelper;

	/**
	 * Every time application startup completed it will pull all records from the DB to cache.
	 */
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.info("in ApplicationListener#onApplicationEvent()");
		try {
			serverDetailsHelper.pullAllServerDetailsToCacheFromDB();
		} catch (Exception e) {
			logger.info("pullAllServerDetailsToCacheFromDB failed");
		}
	}
}
