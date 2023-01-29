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
public class FriendController {
	@Autowired
	UserRepo userRepo;
	
	//	Add Friend to an existing user. (Put)
	
	@PutMapping("/api/user/{userid}/friend")
	public ResponseEntity<Map<String, String>> addFriend(@PathVariable("userid") long userid, @RequestBody Map<String, Long> friend) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		List<Map<String, Object>> user = userRepo.getUsersById(friend.get("friendid"));
		if(user.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		Map<String, Long> mp = userRepo.userFriendExists(userid, friend.get("friendid"));
		if(mp.size() == 0) {
			userRepo.addUserFriends(friend.get("friendid"), userid);
		}
		
		return new ResponseEntity<Map<String,String>>(
				new HashMap<String, String>(){{
					put("message", "Friend added Successfully");
				}}, HttpStatus.OK);
	}
	
	//	Get Friends of a particular user. (Get)
	
	@GetMapping("/api/user/{userid}/friend")
	public List<Map<String, Object>> getFriends(@PathVariable("userid") long userid) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		List<Map<String, Object>> friendList = userRepo.getFriendsOfUser(userid);
		return friendList;
	}
	
	//	Remove the Friend of the user. (Put)
	
	@PutMapping("/api/user/{userid}/friend/{friendid}")
	public ResponseEntity<Map<String, String>> deleteFriend(@PathVariable("userid") long userid, @PathVariable("friendid") long friendid) {
		List<Map<String, Object>> userList = userRepo.getUsersById(userid);
		if(userList.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		
		List<Map<String, Object>> user = userRepo.getUsersById(friendid);
		if(user.size() == 0) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id doesn't exist");
		}
		userRepo.deleteFriendOfUser(userid, friendid);
		return new ResponseEntity<Map<String,String>>(
				new HashMap<String, String>(){{
					put("message", "Friend Removed Successfully");
				}}, HttpStatus.OK);
	}
}
