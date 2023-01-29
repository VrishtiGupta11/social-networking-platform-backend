package com.example.demo.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class MailService {
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	public String sendTextEmail(String emailid, String contentString) throws IOException {
    	String SENDGRID_API_KEY = "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";
    	// the sender email should be the same as we used to Create a Single Sender Verification
	    
    	
    	Email from = new Email("ivrishtigupta@gmail.com");
	    String subject = "Event Registration";
	    Email to = new Email("temp39077@gmail.com");
//	    String contentString = "Successfully registered for the event: "+ eventName + "\nThanks";
	    Content content = new Content("text/plain", contentString);
	    Mail mail = new Mail(from, subject, to, content);
	
	    SendGrid sg = new SendGrid(SENDGRID_API_KEY);
	    Request request = new Request();
	    
	    try {
	      request.setMethod(Method.POST);
	      request.setEndpoint("mail/send");
	      request.setBody(mail.build());
	      Response response = sg.api(request);
	      
	      logger.info(response.getBody());
	      System.out.println(response.getBody());
	      System.out.println(response);
	      
	      return response.getBody();	     
	    } 
	    catch (IOException ex) {
	      throw ex;
	    }
    }
}
