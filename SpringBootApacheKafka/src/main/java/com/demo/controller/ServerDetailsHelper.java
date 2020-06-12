package com.demo.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.demo.model.ServerDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
	@Autowired
	private MongoTemplate mongoTemplate;
	/**
	 * This method will return server details from database,For given server ID.
	 * 
	 * @param serverDetail
	 * @return
	 */
	public ServerDetail getDetailsFromDB(ServerDetail serverDetail) {
		return ServerDetailsRepository.findOne(serverDetail.getId());
	}

	/**
	 * put object into cache.
	 * @param serverDetail
	 */
	public void cacheObject(ServerDetail serverDetail) {
		Cache cache = cacheManager.getCache("serverDetailCache");
		cache.put(serverDetail.getId(), serverDetail);
	}
	
	/**
	 * update db object
	 * @param serverDetail
	 */
	public void updateDbObject(ServerDetail serverDetail) {
		  ServerDetailsRepository.save(serverDetail);
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
	/**
	 * get object into cache.
	 * @param serverDetail
	 * @return 
	 */
	public ServerDetail getCachedObject(String id) {
		Cache cache = cacheManager.getCache("serverDetailCache");
		return cache.get(id, ServerDetail.class);
	}
	
	/**
	 * Load fake data in DB.
	 * @return
	 */
	public CompletableFuture<String> loadFakeData() {
		ServerDetail serverDetail = new ServerDetail();
		String msg="Execution completed !";
		for(int i=1; i<70000;i++) {
			
			try {
				//Fake data
				Faker faker = new Faker();
				serverDetail.setId(String.valueOf(i));
				serverDetail.setName(faker.company().name());
				serverDetail.setHostname(faker.university().name());
				serverDetail.setType(faker.company().industry());
				ServerDetailsRepository.insert(serverDetail);
			} catch (Exception e) {
				logger.error(e.getMessage());
				msg="Somthing went wrong !";
				//break;
			}
			
		}
		List<ServerDetail> serverDetailList = ServerDetailsRepository.findAll();
		logger.info(serverDetailList.get(0).getName());
		logger.info("Thread.currentThread().getName() "+Thread.currentThread().getName()+"  Thread.currentThread().getId()"+Thread.currentThread().getId());
		
		return CompletableFuture.completedFuture(msg);
	}
	
	/**
	 * This method will put server details in cache.
	 * 
	 * @param serverDetail
	 * @return
	 */
	public void getDetailsFromDBWithPagination() {
		   
			 DBCollection collection = mongoTemplate.getCollection("serverDetail");
		     DBCursor cursor = collection.find();        
		     while(cursor.hasNext()){
		         DBObject obj = cursor.next();
		         ServerDetail serverDetail=new ServerDetail(
		        		 obj.get("_id").toString(), 
		        		 obj.get("name").toString(), 
		        		 "", 
		        		 obj.get("hostname").toString(), 
		        		 obj.get("type").toString(),
		        		 "");
		        Cache cache = cacheManager.getCache("serverDetailCache");
		 		cache.put(serverDetail.getId(), serverDetail);
		 		logger.info(serverDetail.getId()+serverDetail.getName());
		     }
		 
	}
	 @Async
	 public void pullAllServerDetailsToCacheFromDB() throws Exception {
	        final long start = System.currentTimeMillis();
	        logger.info("Putting Record in cache");
	        getDetailsFromDBWithPagination();
	        logger.info("Elapsed time: {}", (System.currentTimeMillis() - start));
	  }
	
}
