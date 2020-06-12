package com.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.demo.model.ServerDetail;

/**
 * Kafka consumer class.
 * 
 * @author vinayak
 *
 */
@Service
public class KafkaConsumer {
	private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	@Autowired
	com.demo.controller.ServerDetailsHelper serverDetailsHelper;
	
	@Autowired
	SimpMessagingTemplate template;

	/**
	 * Kafka consume method.
	 * 
	 * @param message
	 */
	@KafkaListener(topics = "ServerStatus")
	public void consume(String message) {
		final long timestamp = System.currentTimeMillis();
		ServerDetail serverDetailKafka = serverDetailsHelper.parseString(message);
		ServerDetail serverDetailfromCache = serverDetailsHelper.getCachedObject(serverDetailKafka.getId());
		//object not present in cache
		if(serverDetailfromCache ==null) {
			//get it from db
			serverDetailfromCache=serverDetailsHelper.getDetailsFromDB(serverDetailKafka);
		}
		serverDetailfromCache.setStatus(serverDetailKafka.getStatus());
		serverDetailfromCache.setLastUpdateTimestamp(String.valueOf(timestamp));
		// update cache
		serverDetailsHelper.cacheObject(serverDetailfromCache);
		// update db
		serverDetailsHelper.updateDbObject(serverDetailfromCache);
		logger.info(String.format("$$ -> Consumed Message -> %s", serverDetailfromCache));
		template.convertAndSend("/topic/greetings", serverDetailfromCache);
	}

}
