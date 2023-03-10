package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Interest;
import com.example.demo.model.User;
import com.example.demo.model.UserLogin;
import com.example.demo.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:49689")
@RestController
public class UserController {
	
	@Autowired
	UserRepo userRepo;
	
	//	Add user/Register user ( Post)
	//	POST /api/user
	
	@PostMapping("/api/user")
	public ResponseEntity<Map<String, Long>> addUser(@RequestBody User user) {
		if(user.getEmail() == null || user.getFirstName() == null || user.getLastName() == null || user.getEmail() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body error");
		}
		Map<String, Object> userMap = userRepo.emailExits(user.getEmail());
		if(userMap.size() > 0) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email Already exists");
		}
		userRepo.save(user);
		return new ResponseEntity<Map<String, Long>>(
				new HashMap<String, Long>(){{
					put("userid", user.getUserId());
				}}, HttpStatus.OK);
	}
	
	// Login
	
	@PostMapping("/api/user/login")
	public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserLogin userLogin) throws JsonProcessingException {
		if(userLogin.getEmail() == null || userLogin.getPassword() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body error");
		}
		Map<String, Object> mp = userRepo.validateUser(userLogin.getEmail(), userLogin.getPassword());
		if(mp.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email or password");
		}
		return new ResponseEntity<Map<String, Object>>(mp, HttpStatus.OK);
	}
	
	
	//	Get user by user id and display user information. (Get)
	
	@GetMapping("/api/user/{userid}")
	public Map<String, Object> getUser(@PathVariable("userid") long userid) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		User user = userRepo.getReferenceById(userid);
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("userid", user.getUserId());
		responseMap.put("firstName", user.getFirstName());
		responseMap.put("lastName", user.getLastName());
		responseMap.put("email", user.getEmail());
		responseMap.put("city", user.getCity());
		
		return responseMap;
	}
	
	//	Delete a user ( Delete)
	
	@DeleteMapping("/api/user/{userid}")
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable("userid") long userid) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		userRepo.deleteById(userid);
		return new ResponseEntity<Map<String,String>>(
				new HashMap<String, String>(){{
					put("message", "User Deleted Successfully");
				}}, HttpStatus.OK);
	}
	
	// Get Users of particular interest
	@GetMapping("/api/interest/{interestid}/user")
	public ResponseEntity<List<Map<String, Object>>> getUserOfParticularInterest(@PathVariable("interestid") long interestid) {
		List<Map<String, Object>> usersOfInterest = userRepo.getUsersOfInterest(interestid);
		return new ResponseEntity<List<Map<String, Object>>>(usersOfInterest, HttpStatus.OK);
	}
}
