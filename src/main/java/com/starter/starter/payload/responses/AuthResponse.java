package com.starter.starter.payload.responses;

import java.io.Serializable;
import java.util.List;



public class AuthResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final Long id;
	private final String username;
	private final String email;
	private List<String> roles;

	public AuthResponse(String jwttoken, Long id, String username, String email) {
		this.id = id;
		this.jwttoken = jwttoken;
		this.username = username;
		this.email = email;
	}

	public AuthResponse(String jwttoken, Long id, String username, String email, List<String> roles) {
		this.jwttoken = jwttoken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.roles = roles;
	}

	public String getJwttoken() {
		return jwttoken;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public List<String> getRoles() {
		return roles;
	}
	
}