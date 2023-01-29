package com.example.demo.controller;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:49689")
@RestController
public class EmailController {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@PostMapping("/api/mail")
	public ResponseEntity<Map<String, String>> sendMail(@RequestBody Map<String, String> email) throws IOException {
		SimpleMailMessage message = new SimpleMailMessage();
		
		String from = "temp39077@gmail.com";
		String to = email.get("email");
		
		message.setFrom(from);
		message.setTo(to);
		message.setSubject("Event Registration");
		message.setText("Hey " + email.get("name") + ",\n" +"You Have Successfully registered for the event- " + email.get("eventName") + ".\n\nThank you");
		 
		javaMailSender.send(message);
		
		return new ResponseEntity<Map<String,String>>(
				new HashMap<String, String>(){{
					put("message", "Mail Sent");
				}}, HttpStatus.OK);
	}
}