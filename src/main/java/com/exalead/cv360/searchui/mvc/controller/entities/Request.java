package com.exalead.cv360.searchui.mvc.controller.entities;

import java.time.LocalDateTime;

public class Request {

	private Integer id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	// foreign key
	private Integer refMotif;
	private Integer refInteraction;
	
	
	public Request(Integer id, String name, Integer refMotif, Integer refInteraction) {
		super();
		this.id = id;
		this.name = name;
		this.refMotif = refMotif;
		this.refInteraction = refInteraction;
	}
	public Request(String name, Integer refMotif, Integer refInteraction) {
		super();
		this.name = name;
		this.refMotif = refMotif;
		this.refInteraction = refInteraction;
	}
	public Request() {
		
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
	public Integer getRefMotif() {
		return refMotif;
	}
	public void setRefMotif(Integer refMotif) {
		this.refMotif = refMotif;
	}
	public Integer getRefInteraction() {
		return refInteraction;
	}
	public void setRefInteraction(Integer refInteraction) {
		this.refInteraction = refInteraction;
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
