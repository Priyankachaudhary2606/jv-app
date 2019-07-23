package com.inverter.jakson.dto;

public class StoreUserDetailsDto {

	private String jkusername;
	private String jkpassword;
	private String authToken;
	
	
	
	public StoreUserDetailsDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public StoreUserDetailsDto(String jkusername, String jkpassword, String authToken) {
		super();
		this.jkusername = jkusername;
		this.jkpassword = jkpassword;
		this.authToken = authToken;
	}
	/**
	 * @return the jkusername
	 */
	public String getJkusername() {
		return jkusername;
	}
	/**
	 * @param jkusername the jkusername to set
	 */
	public void setJkusername(String jkusername) {
		this.jkusername = jkusername;
	}
	/**
	 * @return the jkpassword
	 */
	public String getJkpassword() {
		return jkpassword;
	}
	/**
	 * @param jkpassword the jkpassword to set
	 */
	public void setJkpassword(String jkpassword) {
		this.jkpassword = jkpassword;
	}
	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}
	/**
	 * @param authToken the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}


}
