package com.inverter.jakson.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;



@Component
@PropertySource("classpath:sql/storeUserDetails.yml")
@ConfigurationProperties(prefix="storeusertoken")
public class StoreUserDetailsConfig {
	 

	 @Value("${saveNewUserSql}")	
	private String saveNewUserSql;


	/**
	 * @return the saveNewUserSql
	 */
	public String getSaveNewUserSql() {
		return saveNewUserSql;
	}


	/**
	 * @param saveNewUserSql the saveNewUserSql to set
	 */
	public void setSaveNewUserSql(String saveNewUserSql) {
		this.saveNewUserSql = saveNewUserSql;
	}
	 
	 
	 

}
