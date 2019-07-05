package com.inverter.jakson.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;



@Component
@PropertySource("classpath:sql/storeUserDetails.yml")
@ConfigurationProperties(prefix="storeuser")
public class StoreUserDetailsConfig {

	 @Value("${checkUserExistenceSql}")
	private String checkUserExistenceSql;
	 

	 @Value("${saveNewUserSql}")	
	private String saveNewUserSql;


	/**
	 * @return the checkUserExistenceSql
	 */
	public String getCheckUserExistenceSql() {
		return checkUserExistenceSql;
	}


	/**
	 * @param checkUserExistenceSql the checkUserExistenceSql to set
	 */
	public void setCheckUserExistenceSql(String checkUserExistenceSql) {
		this.checkUserExistenceSql = checkUserExistenceSql;
	}


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
