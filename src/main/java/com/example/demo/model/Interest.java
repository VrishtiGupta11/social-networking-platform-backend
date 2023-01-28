package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "interest")
public class Interest {
	@Id
	@Column(name = "interestId")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long interestId;
	
	@Column(name="interestName")
	private String interestName;
	
	
	public long getInterestId() {
		return interestId;
	}
	public void setInterestId(long interestId) {
		this.interestId = interestId;
	}
	public String getInterestName() {
		return interestName;
	}
	public void setInterestName(String interestName) {
		this.interestName = interestName;
	}
	
	public Interest() {
		super();
	}
}
