package com.demo.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.demo.model.ServerDetail;
import com.demo.service.KafkaProducer;
/**
 * Controller class, For various rest operation.
 * @author vinayak
 *
 */
@RestController
@RequestMapping(value = "/demo-kafka/")
public class ApacheKafkaWebController {
	private final Logger logger = LoggerFactory.getLogger(ApacheKafkaWebController.class);

	@Autowired
	com.demo.repository.ServerDetailsRepository ServerDetailsRepository;
	
	@Autowired
	com.demo.controller.ServerDetailsHelper serverDetailsHelper;
	
	@Autowired
	KafkaProducer kafkaProducer;

    /**
     * Get Status of specific Server.
     * @return 
     */
	@GetMapping(value = "/serverDetailsFromDb")
	public ServerDetail getserverDetails(@RequestParam String id) {
		ServerDetail serverDetail = ServerDetailsRepository.findOne(id);
		logger.info(serverDetail.toString());
		return serverDetail;
	}
	
	/**
     * Load fake bulk data at MongoDB.Extected time to load data 5 minutes.
	 * @return 
     */
	@GetMapping(value = "/loadBulkTestData")
	public String loadBulkTestData(@RequestParam int noOfMessages) {
		CompletableFuture<String> result=serverDetailsHelper.loadFakeData(noOfMessages);
		String msg="Somthing went wrong !";
		try {
			msg =result.get();
		} catch (Exception e) {
			logger.error(e.getMessage());
			msg="Somthing went wrong !";
		} 
		return msg;
	}
	
	/**
	 * Publish bulk messages to kafka.
	 * @return 
	 */
	@GetMapping(value = "/sendMessageToKafkaTopic")
	public String sendMessageToKafkaTopic(@RequestParam int noOfMessages){
		String message="";
		ServerDetail serverDetail = new ServerDetail();
		
		for (int i = 1; i <= noOfMessages; i++) {
			try {
				//serverDetail.setId(String.valueOf(ServerDetailsHelper.getRandomDoubleBetweenRange(1, 70000)));
				serverDetail.setId(String.valueOf(i));
				serverDetail.setStatus(ServerDetailsHelper.getRandomDoubleBetweenRange(1, 2)%2==0?"Active":"inactive");
				message=serverDetailsHelper.getJsonString(serverDetail);
				kafkaProducer.sendMessage(message);
			} catch (Exception e) {
				logger.error(e.getMessage());
				// break;
			}

		}
		
		return noOfMessages +" Messages published !";
	}
	
	/**
	 * Publish specific messages to kafka.
	 * @return 
	 */
	@GetMapping(value = "/publishStatusForGivenId")
	public String publishStatusForGivenId(@RequestParam int id,@RequestParam String status){
		String message="";
		ServerDetail serverDetail = new ServerDetail();
			try {
				serverDetail.setId(String.valueOf(id));
				serverDetail.setStatus(status);
				message=serverDetailsHelper.getJsonString(serverDetail);
				kafkaProducer.sendMessage(message);
			} catch (Exception e) {
				logger.error(e.getMessage());
				// break;
			}
		return message +" published !";
	}
	
	/**
	 * Get Server details form cache.
	 * @return 
	 */
	@GetMapping(value = "/getServerDetailsfromCache")
	public ServerDetail getServerDetailsfromCache(@RequestParam String id){
		ServerDetail serverDetailDb = ServerDetailsRepository.findOne(id);
		ServerDetail serverDetailCache = serverDetailsHelper.getCachedObject(id);
		logger.info("Cached object: "+serverDetailCache);
		logger.info("DB object: "+serverDetailDb);

		return serverDetailCache;
	}

}