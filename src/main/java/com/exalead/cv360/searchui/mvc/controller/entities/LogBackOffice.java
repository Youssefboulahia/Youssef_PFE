package com.exalead.cv360.searchui.mvc.controller.entities;

public class LogBackOffice {

	String date;
	String login;
	String module;
	String operation;
	String idObj;
	String status;
	String message;
	
	public LogBackOffice(String date, String login, String module, String operation, String idObj, String status,
			String message) {
		super();
		this.date = date;
		this.login = login;
		this.module = module;
		this.operation = operation;
		this.idObj = idObj;
		this.status = status;
		this.message = message;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getIdObj() {
		return idObj;
	}

	public void setIdObj(String idObj) {
		this.idObj = idObj;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
