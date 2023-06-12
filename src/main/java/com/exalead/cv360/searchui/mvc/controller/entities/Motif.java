package com.exalead.cv360.searchui.mvc.controller.entities;


import java.time.LocalDateTime;

public class Motif {
	
	private Integer id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	// foreign key
	private Integer refCategory;
	
	public Motif(Integer id, String name, Integer refCategory) {
		super();
		this.id = id;
		this.name = name;
		this.refCategory = refCategory;
	}
	
	//call when update
	public Motif(Integer id, String name, LocalDateTime updateDate) {
		super();
		this.id = id;
		this.name = name;
		this.updatedAt = updateDate;
	}
	public Motif(Integer id, String name, int refCategory, LocalDateTime updateDate) {
		super();
		this.id = id;
		this.name = name;
		this.refCategory = refCategory;
		this.updatedAt = updateDate;
	}
	
	//call when create
	public Motif(String name, Integer refCategory, LocalDateTime date) {
		super();
		this.name = name;
		this.refCategory = refCategory;
		this.createdAt = date;
		this.updatedAt = null;
	}
	public Motif() {
		
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
	public Integer getRefCategory() {
		return refCategory;
	}
	public void setRefCategory(Integer refCategory) {
		this.refCategory = refCategory;
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
