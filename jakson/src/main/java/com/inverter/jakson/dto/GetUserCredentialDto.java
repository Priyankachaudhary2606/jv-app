package com.inverter.jakson.dto;

public class GetUserCredentialDto {
	
	private String jkusername;
	private String jkpassword;
	private String displayname;
	private String voiceid;
	private Integer mobileno;
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
	 * @return the displayname
	 */
	public String getDisplayname() {
		return displayname;
	}
	/**
	 * @param displayname the displayname to set
	 */
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	/**
	 * @return the voiceid
	 */
	public String getVoiceid() {
		return voiceid;
	}
	/**
	 * @param voiceid the voiceid to set
	 */
	public void setVoiceid(String voiceid) {
		this.voiceid = voiceid;
	}
	/**
	 * @return the mobileno
	 */
	public Integer getMobileno() {
		return mobileno;
	}
	/**
	 * @param mobileno the mobileno to set
	 */
	public void setMobileno(Integer mobileno) {
		this.mobileno = mobileno;
	}
	public GetUserCredentialDto(String jkusername, String jkpassword, String displayname, String voiceid,
			Integer mobileno) {
		super();
		this.jkusername = jkusername;
		this.jkpassword = jkpassword;
		this.displayname = displayname;
		this.voiceid = voiceid;
		this.mobileno = mobileno;
	}
	public GetUserCredentialDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
