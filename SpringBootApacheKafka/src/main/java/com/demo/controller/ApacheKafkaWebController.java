package com.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.demo.model.ServerDetail;
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

}