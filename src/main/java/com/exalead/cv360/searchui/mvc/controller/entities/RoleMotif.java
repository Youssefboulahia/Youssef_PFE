package com.exalead.cv360.searchui.mvc.controller.entities;

import java.time.LocalDateTime;

public class RoleMotif {

	private Integer id;
	private Boolean status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	// foreign key
	private Integer refRole;
	private Integer refMotif;
	
	public RoleMotif(Integer id, Boolean status, Integer refRole, Integer refMotif) {
		super();
		this.id = id;
		this.status = status;
		this.refRole = refRole;
		this.refMotif = refMotif;
	}
	
	public RoleMotif(Boolean status, Integer refRole, Integer refMotif, LocalDateTime date) {
		super();
		this.status = status;
		this.refRole = refRole;
		this.refMotif = refMotif;
		this.createdAt = date;
		this.updatedAt = null;
	}
	
	public RoleMotif(Integer id , Boolean status) {
		super();
		this.status = status;
	}
	
	public RoleMotif() {
		
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getRefRole() {
		return refRole;
	}
	public void setRefRole(Integer refRole) {
		this.refRole = refRole;
	}
	public Integer getRefMotif() {
		return refMotif;
	}
	public void setRefMotif(Integer refMotif) {
		this.refMotif = refMotif;
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
