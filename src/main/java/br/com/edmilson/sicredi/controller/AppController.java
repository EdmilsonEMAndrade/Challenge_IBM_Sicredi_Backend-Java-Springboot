package br.com.edmilson.sicredi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class AppController {

	@Value("${app.message}")
	private String appMessage;
	
	@GetMapping("/")
	public String appMessage() {
		return appMessage;
	}
}
