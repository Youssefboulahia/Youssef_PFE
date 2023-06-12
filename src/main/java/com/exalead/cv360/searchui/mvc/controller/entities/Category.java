package com.exalead.cv360.searchui.mvc.controller.entities;

import java.time.LocalDateTime;

public class Category {
	
	private Integer id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	// foreign key
	private Integer refSubject;
	
	public Category(Integer id, String name, Integer refSubject) {
		super();
		this.id = id;
		this.name = name;
		this.refSubject = refSubject;
	}
	
	//call when updating
	public Category(Integer id, String name, LocalDateTime dateUpdate) {
		super();
		this.id = id;
		this.name = name;
		this.updatedAt = dateUpdate;
	}
	public Category(Integer id, String name, int refSubject ,LocalDateTime dateUpdate) {
		super();
		this.id = id;
		this.name = name;
		this.refSubject = refSubject;
		this.updatedAt = dateUpdate;
	}
	
	//call when creating
	public Category(String name, Integer refSubject, LocalDateTime date) {
		super();
		this.name = name;
		this.refSubject = refSubject;
		this.createdAt = date;
		this.updatedAt = null;
	}
	public Category() {
		
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
	public Integer getRefSubject() {
		return refSubject;
	}
	public void setRefSubject(Integer refSubject) {
		this.refSubject = refSubject;
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
