package com.example.demo.controller;

import com.example.demo.service.MailService;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:49689")
@RestController
public class EmailController {
	
	@Autowired
	MailService mailService;
	
	@PostMapping("/api/mail")
	public String sendMail(@RequestBody Map<String, String> email) throws IOException {
		return mailService.sendTextEmail(email.get("email"), email.get("content"));
	}
}