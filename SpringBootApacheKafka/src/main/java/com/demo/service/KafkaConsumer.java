package com.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.demo.model.ServerDetail;
/**
 * Kafka consumer class.
 * @author vinayak
 *
 */
@Service
public class KafkaConsumer {
	private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	@Autowired
	com.demo.controller.ServerDetailsHelper serverDetailsHelper;

	/**
	 * Kafka consume method.
	 * @param message
	 */
	@KafkaListener(topics = "ServerStatus")
	public void consume(String message) {
		ServerDetail serverDetailKafka = serverDetailsHelper.parseString(message);
		ServerDetail serverDetailDB = serverDetailsHelper.getDetailsFromDB(serverDetailKafka);
		if(serverDetailDB!=null) {
			serverDetailDB.setStatus(serverDetailKafka.getStatus());
			logger.info(String.format("$$ -> Consumed Message -> %s", serverDetailDB));
			serverDetailsHelper.cacheObject(serverDetailDB);
		}else {
			logger.info(String.format("Not Found Server details at DB for Server ID:%s", serverDetailKafka.getId()));
		}
		
	}

	
	
	
}
