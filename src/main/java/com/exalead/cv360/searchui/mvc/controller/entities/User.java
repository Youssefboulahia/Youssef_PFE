package com.exalead.cv360.searchui.mvc.controller.entities;

import java.time.LocalDateTime;

public class User {

	private Integer id;
	private String login;
	private String password;
	private String displayname;
	private Boolean isActive;
	private Boolean isAdmin;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	// foreign key
	private Integer refRole;
	

	public User(int id, String login,String password, String displayname, int refRole, LocalDateTime createDate) {
		super();
		this.id = id;
		this.login = login;
		this.password = password;
		this.displayname = displayname;
		this.isActive = true;
		this.isAdmin = false;
		this.refRole = refRole;
		this.createdAt = createDate;
		this.updatedAt = null;
	}
	
	public User(String login,String password, String displayname,Boolean isActive, Boolean isAdmin, int refRole, LocalDateTime createDate) {
		super();
		this.login = login;
		this.password = password;
		this.displayname = displayname;
		this.isActive = isActive;
		this.isAdmin = isAdmin;
		this.refRole = refRole;
		this.createdAt = createDate;
		this.updatedAt = null;
	}
	public User() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayname() {
		return displayname;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
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

	public Integer getRefRole() {
		return refRole;
	}

	public void setRefRole(Integer refRole) {
		this.refRole = refRole;
	}
	
	
	
}
