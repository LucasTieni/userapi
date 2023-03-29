package com.payconomy.userapi.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
	DATA_INVALID("data-invalid", "Data invalid"),
	DATA_CONFLICT("data-conflict", "Data conflict"),
	SYSTEM_ERROR("system-error", "System error"),
	PARAMETER_INVALID("parameter-invalid", "Parameter invalid"),
	MESSAGE_INCOMPREHENSIBLE("message-incomprehensible", "Message incomprehensible"),
	RESOURCE_NOT_FOUND("resource-not-found", "Resource not found"),
	ENTITY_IN_USE("entity-in-use", "Entity in use"),
	BUSINESS_ERROR("business-error", "Business logic violation"),
	ACCESS_DENIED("/acess-denied", "Access denied");
	
	private String title;
	private String uri;
	
	ProblemType(String path, String title){
		this.uri = "https://userapi.com.br/" + path;
		this.title = title;
	}
	
}
