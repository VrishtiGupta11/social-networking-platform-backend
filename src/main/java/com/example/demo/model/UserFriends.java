package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userFriends")
public class UserFriends {
	@Id
	private long userId;
	@Id
	private long friendId;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getFriendId() {
		return friendId;
	}
	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
	public UserFriends(long userId, long friendId) {
		super();
		this.userId = userId;
		this.friendId = friendId;
	}
	public UserFriends() {
		super();
	}
}
