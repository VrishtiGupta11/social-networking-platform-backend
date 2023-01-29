package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Interest;
import com.example.demo.repo.UserRepo;

@CrossOrigin(origins = "http://localhost:49689")
@RestController
public class InterestController {
	@Autowired
	UserRepo userRepo;
	
	//	Add interest to an existing user. (Put)
	//	PUT /api/user/:id/interest
	
	@PutMapping("/api/user/{userid}/interest")
	public ResponseEntity<Map<String, String>> addInterest(@PathVariable("userid") long userid, @RequestBody Interest interest) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		List<Map<String, Object>> interestList = userRepo.getInterestById(interest.getInterestId());
		if(interestList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest id doesn't exist");
		}
		
		Map<String, Long> mp = userRepo.userInterestExists(userid, interest.getInterestId());
		if(mp.size() == 0) {
			userRepo.updateInterest(interest.getInterestId(), userid);
		}
		
		return new ResponseEntity<Map<String,String>>(
				new HashMap<String, String>(){{
					put("message", "Interest Inserted Successfully");
				}}, HttpStatus.OK);
	}
	
	//	Get interests of a particular user. (Get)
	
	@GetMapping("/api/user/{userid}/interest")
	public List<Map<String, Object>> getInterest(@PathVariable("userid") long userid) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		List<Map<String, Object>> interestList = userRepo.getInterestsOfUser(userid);
		return interestList;
	}
	
	//	Remove the interest of the user. (Put)
	
	@PutMapping("/api/user/{userid}/interest/{interestid}")
	public ResponseEntity<Map<String, String>> deleteInterest(@PathVariable("userid") long userid, @PathVariable("interestid") long interestid) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		List<Map<String, Object>> interestList = userRepo.getInterestById(interestid);
		if(interestList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest id doesn't exist");
		}
		userRepo.deleteInterestOfUser(userid, interestid);
		return new ResponseEntity<Map<String,String>>(
				new HashMap<String, String>(){{
					put("message", "Interest Removed Successfully");
				}}, HttpStatus.OK);
	}
}
