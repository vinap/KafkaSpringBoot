package com.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.demo.model.ServerDetail;

@Controller
public class DashboardController {

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public ServerDetail greeting(String message) throws Exception {
		return new ServerDetail();
	}
}
