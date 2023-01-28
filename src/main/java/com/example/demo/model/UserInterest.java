package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userInterest")
public class UserInterest {
	@Id
	private long interestId;
	@Id
	private long userId;
	
	public long getInterestId() {
		return interestId;
	}
	public void setInterestId(long interestId) {
		this.interestId = interestId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
}