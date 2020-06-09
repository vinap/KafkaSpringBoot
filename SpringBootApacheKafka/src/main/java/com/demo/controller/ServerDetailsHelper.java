package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.demo.model.ServerDetail;


/**
 * Helper class for DB operations.
 * @author vinayak
 *
 */

@Component
public class ServerDetailsHelper {
	@Autowired
	com.demo.repository.ServerDetailsRepository ServerDetailsRepository;
/**
 * This method will return server details from database,For given server ID. 
 * @param serverDetail
 * @return
 */ @Cacheable(cacheNames="serverDetailCache")
	public ServerDetail getDetailsFromDB(ServerDetail serverDetail) {
		return ServerDetailsRepository.findOne(serverDetail.getId());
	}

}
