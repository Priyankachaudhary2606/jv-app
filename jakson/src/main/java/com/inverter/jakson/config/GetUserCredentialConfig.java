package com.inverter.jakson.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:sql/getUserCredential.yml")
@ConfigurationProperties(prefix="jaksonUserCredential")
public class GetUserCredentialConfig {

	@Value("${getUserCredential}")
	private String userCredential;

	/**
	 * @return the userCredential
	 */
	public String getUserCredential() {
		return userCredential;
	}

	/**
	 * @param userCredential the userCredential to set
	 */
	public void setUserCredential(String userCredential) {
		this.userCredential = userCredential;
	}
	
	
	
}
