package com.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.demo.model.ServerDetail;
import com.demo.service.KafkaProducer;
import com.github.javafaker.Faker;
/**
 * Controller class.
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
     * Added this method for debugging only.Remove this later
     */
	@GetMapping(value = "/serverDetails")
	public void getserverDetails() {
		ServerDetail serverDetail = new ServerDetail();
		serverDetail.setId("2");
		serverDetail.setServerName("Pune");
		List<ServerDetail> serverDetailList = ServerDetailsRepository.findAll();
		logger.info(serverDetailList.get(0).getServerName());
	}
	
	/**
     * Added this method for debugging only.Remove this later
     */
	@GetMapping(value = "/LoadTestServerDetails")
	public void LoadTestServerDetails() {
		ServerDetail serverDetail = new ServerDetail();
		for(int i=70000; i<100000;i++) {
			
			try {
				//Fake data
				Faker faker = new Faker();
				String streetAddress = faker.address().streetAddress();
				serverDetail.setId(String.valueOf(i));
				serverDetail.setServerName(streetAddress);
				ServerDetailsRepository.insert(serverDetail);
			} catch (Exception e) {
				logger.error(e.getMessage());
				//break;
			}
			
		}
		List<ServerDetail> serverDetailList = ServerDetailsRepository.findAll();
		logger.info(serverDetailList.get(0).getServerName());
	}
	
	@GetMapping(value = "/publish")
	public void sendMessageToKafkaTopic(){
		String message="";
		ServerDetail serverDetail = new ServerDetail();
		
		for (int i = 70000; i < 100000; i++) {
			try {
				serverDetail.setId(String.valueOf(ServerDetailsHelper.getRandomDoubleBetweenRange(1, 70000)));
				serverDetail.setServerStatus(ServerDetailsHelper.getRandomDoubleBetweenRange(1, 2)%2==0?"Active":"inactive");
				message=serverDetailsHelper.getJsonString(serverDetail);
				kafkaProducer.sendMessage(message);
			} catch (Exception e) {
				logger.error(e.getMessage());
				// break;
			}

		}
		
		
	}

}