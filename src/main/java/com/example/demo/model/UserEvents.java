package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userEvents")
public class UserEvents {
	@Id
	private long eventId;
	
	@Id
	private long userId;

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public UserEvents(long eventId, long userId) {
		super();
		this.eventId = eventId;
		this.userId = userId;
	}

	public UserEvents() {
		super();
	}
	
}



