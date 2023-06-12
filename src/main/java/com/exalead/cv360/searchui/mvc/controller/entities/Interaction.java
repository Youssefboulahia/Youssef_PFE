package com.exalead.cv360.searchui.mvc.controller.entities;

import java.time.LocalDateTime;

public class Interaction {
	
	private Integer id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	// foreign key
	private Integer refAgent;
	
	public Interaction(Integer id, String name, Integer refAgent) {
		super();
		this.id = id;
		this.name = name;
		this.refAgent = refAgent;
	}
	public Interaction(String name, Integer refAgent) {
		super();
		this.name = name;
		this.refAgent = refAgent;
	}
	public Interaction() {
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getRefAgent() {
		return refAgent;
	}
	public void setRefAgent(Integer refAgent) {
		this.refAgent = refAgent;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	

}
