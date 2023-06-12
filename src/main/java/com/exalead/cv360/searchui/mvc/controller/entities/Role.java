package com.exalead.cv360.searchui.mvc.controller.entities;

import java.time.LocalDateTime;

public class Role {

	private Integer id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	
	public Role(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	//call when update
	public Role(Integer id, String name, LocalDateTime dateUpdate) {
		super();
		this.id = id;
		this.name = name;
		this.updatedAt = dateUpdate;
	}
	//call when create
	public Role( String name, LocalDateTime date) {
		super();
		this.name = name;
		this.createdAt = date;
		this.updatedAt = null;
	}
	
	public Role() {
		
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
