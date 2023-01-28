package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Events;
import com.example.demo.model.Interest;
import com.example.demo.repo.EventsRepo;
import com.example.demo.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@CrossOrigin(origins = "http://localhost:49689")
@RestController
public class EventController {
	
	@Autowired
	EventsRepo eventsRepo;
	
	@Autowired
	UserRepo userRepo;
	
	private HttpHeaders createHttpHeaders() {
		String encodedAuth = "S2KLVN2WTEE2JLI2K4XR";
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("Authorization", "Bearer " + encodedAuth);
	    return headers;
	}
	
	//	Add a new event. (Post)
	
	@PostMapping("/api/event")
	public ResponseEntity<Map<String, String>> addEvent(@RequestBody Object object) throws JsonProcessingException {
		
		String theUrl = "https://www.eventbriteapi.com/v3/organizations/1362496999733/events/";
	    RestTemplate restTemplate = new RestTemplate();
	    try {
	        HttpHeaders headers = createHttpHeaders();
	        ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> entity = new HttpEntity<String>(objectMapper.writeValueAsString(object), headers);
	        ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.POST, entity, String.class);
	        System.out.println(response.getBody());
	        
	        Map<String, Object> mp = new ObjectMapper().readValue(response.getBody(), HashMap.class);

	        Events events = new Events(Long.parseLong(mp.get("id").toString()));
	        eventsRepo.save(events);
	    }
	    catch (Exception eek) {
	        System.out.println("** Exception: "+ eek.getMessage());
	    }
		
		return new ResponseEntity<Map<String,String>>(
				new HashMap<String, String>(){{
					put("message", "Event Added Successfully");
				}}, HttpStatus.OK);
	}
	
	//	Get all the events. (Get)
	
	@GetMapping("/api/event")
	public Object getEvents() throws JsonMappingException, JsonProcessingException {
		String theUrl = "https://www.eventbriteapi.com/v3/organizations/1362496999733/events/";
		RestTemplate restTemplate = new RestTemplate();
		Object object = new Object();
	    try {
	        HttpHeaders headers = createHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
	        
	        Map<String, Object> mp = new ObjectMapper().readValue(response.getBody(), HashMap.class);

	        object = mp.get("events");
	        System.out.println(object);
	    }
	    catch (Exception eek) {
	        System.out.println("** Exception: "+ eek.getMessage());
	    }
	    
	    return object;

	}
	
	// Get Single event (Get)
	
	@GetMapping("/api/event/{eventid}")
	public Object getEvent(@PathVariable("eventid") long eventid) {
		List list = eventsRepo.getEventById(eventid);
		if(list.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event id doesn't exist");
		}
		
		String theUrl = "https://www.eventbriteapi.com/v3/events/"+Long.toString(eventid)+"/";
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> mp = new HashMap<>();
	    try {
	        HttpHeaders headers = createHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
	        
	        mp = new ObjectMapper().readValue(response.getBody(), HashMap.class);
	    }
	    catch (Exception eek) {
	        System.out.println("** Exception: "+ eek.getMessage());
	    }
	    return mp;
	}
	
	// Get events happening for a particular interest
	@GetMapping("/api/user/{userid}/event")
	public ResponseEntity<List<Map<String, Object>>> getEventsBasedOnInterest(@PathVariable("userid") long userid) throws JsonMappingException, JsonProcessingException {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		Object object = getEvents();

		ObjectMapper objectMapper = new ObjectMapper();
		List list = objectMapper.convertValue(object, List.class);
		
		List<Map<String, Object>> interestList = userRepo.getInterestsOfUser(userid);
		
		List<Map<String, Object>> eventsBasedOnInterest = new ArrayList<>();
		
		for(Map<String, Object> interestMap : interestList) {
			for(Object element : list) {
				Map<String, Object> map = objectMapper.convertValue(element, Map.class);
				Object nameObject = map.get("name");
				
				Map<String, ?> nameMap = objectMapper.convertValue(nameObject, Map.class);
			
				String interestString = objectMapper.convertValue(nameMap.get("text"), String.class);
				String interestMapString = objectMapper.convertValue(interestMap.get("interestname"), String.class);

				System.out.println(interestMapString + "  " + interestString);
				System.out.println(interestMapString.equals(interestString));

				if(interestMapString.equals(interestString)) {
					eventsBasedOnInterest.add(map);
				}
			}
		}
		System.out.println(eventsBasedOnInterest);
		return new ResponseEntity<List<Map<String, Object>>>(eventsBasedOnInterest, HttpStatus.OK);
	}
	
	
	// Register for the event
	
	@PutMapping("/api/user/{userid}/event") 
	public ResponseEntity<Map<String, String>> resisterEvent(@PathVariable("userid") long userid, @RequestBody Map<String, Long> events) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		List<Map<String, Object>> eventsList = eventsRepo.getEventById(events.get("eventId"));
		if(eventsList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event id doesn't exist");
		}
		
		eventsRepo.updateEvent(events.get("eventId"), userid);
		return new ResponseEntity<Map<String,String>>(
				new HashMap<String, String>(){{
					put("message", "Successfully Registered for the event");
				}}, HttpStatus.OK);
	}
	
	// Get Venue by venueid
	@GetMapping("/api/venue/{venueid}")
	public Map<String, Object> getVenueById(@PathVariable("venueid") String venueid) {
		String theUrl = "https://www.eventbriteapi.com/v3/venues/"+venueid+"/";
		RestTemplate restTemplate = new RestTemplate();
		Map<String, Object> mp = new HashMap<>();
	    try {
	        HttpHeaders headers = createHttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
	        ResponseEntity<String> response = restTemplate.exchange(theUrl, HttpMethod.GET, entity, String.class);
	        
	        mp = new ObjectMapper().readValue(response.getBody(), HashMap.class);
	    }
	    catch (Exception eek) {
	        System.out.println("** Exception: "+ eek.getMessage());
	    }
	    
	    return mp;
	}
	
	
	// Get events of entered city
	
	@GetMapping("/api/user/{userid}/city/event") 
	public ResponseEntity<List<Map<String, Object>>> getEventsBasedOnCity(@PathVariable("userid") long userid) throws JsonMappingException, JsonProcessingException {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		ObjectMapper objectMapper = new ObjectMapper(); 
		String city = objectMapper.convertValue(userList.get(0).get("city"), String.class);
		
		Object object = getEvents();
		List list = objectMapper.convertValue(object, List.class);
		
		List<Map<String, Object>> eventsListBasedOnCity = new ArrayList<>();
		
		for(Object element : list) {
			Map<String, Object> map = objectMapper.convertValue(element, Map.class);
			Object venueid = map.get("venue_id");
			String venueidString = objectMapper.convertValue(venueid, String.class);
			
			Map<String, Object> mp = getVenueById(venueidString);
			Map address = objectMapper.convertValue(mp.get("address"), Map.class);
			String cityString = objectMapper.convertValue(address.get("city"), String.class);
			
			if(cityString.equals(city)) {
				eventsListBasedOnCity.add(map);
			}
		}
		
		return new ResponseEntity<List<Map<String, Object>>>(eventsListBasedOnCity, HttpStatus.OK);
	}
	
	// Get Users of Particular event
	
	@GetMapping("/api/event/{eventid}/user")
	public ResponseEntity<List<Map<String, Object>>> getUsersOfParticularEvent(@PathVariable("eventid") long eventid) {
		List<Map<String, Object>> usersOfEvent = eventsRepo.getUsersOfEvent(eventid);
		return new ResponseEntity<List<Map<String, Object>>>(usersOfEvent, HttpStatus.OK);
	}
}


