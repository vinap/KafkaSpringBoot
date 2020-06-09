package com.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.demo.model.ServerDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		ServerDetail serverDetail = parseString(message);
		serverDetail = serverDetailsHelper.getDetailsFromDB(serverDetail);
		logger.info(String.format("$$ -> Consumed Message -> %s", serverDetail));
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
}
