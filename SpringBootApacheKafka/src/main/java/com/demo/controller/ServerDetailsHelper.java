package com.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.demo.model.ServerDetail;
import com.demo.service.KafkaConsumer;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper class for DB operations.
 * 
 * @author vinayak
 *
 */

@Component
public class ServerDetailsHelper {
	private final Logger logger = LoggerFactory.getLogger(ServerDetailsHelper.class);

	@Autowired
	com.demo.repository.ServerDetailsRepository ServerDetailsRepository;
	@Autowired
	private CacheManager cacheManager;

	/**
	 * This method will return server details from database,For given server ID.
	 * 
	 * @param serverDetail
	 * @return
	 */
	public ServerDetail getDetailsFromDB(ServerDetail serverDetail) {
		return ServerDetailsRepository.findOne(serverDetail.getId());
	}

	public void cacheObject(ServerDetail serverDetail) {
		Cache cache = cacheManager.getCache("serverDetailCache");
		cache.put(serverDetail.getId(), serverDetail);
	}
	
	/**
	 * Return parsed object for given string.
	 * @param message
	 * @return
	 */
	public ServerDetail parseString(String message) {

		ObjectMapper objectMapper = new ObjectMapper();
		ServerDetail serverDetail = null;
		try {
			serverDetail = objectMapper.readValue(message, ServerDetail.class);
		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.info(String.format("invalid messgae :", message));
		}
		return serverDetail;
	}
	

	public static int getRandomDoubleBetweenRange(double min, double max) {
		int x = (int)((Math.random()*((max-min)+1))+min);
		return x;
	}
	
	public String getJsonString(ServerDetail serverDetail) {

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonStr="";
		try {
			jsonStr = objectMapper.writeValueAsString(serverDetail);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return jsonStr;
	}
}
