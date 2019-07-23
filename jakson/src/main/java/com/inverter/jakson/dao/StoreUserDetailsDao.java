package com.inverter.jakson.dao;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.inverter.jakson.common.AbstractTransactionalDao;
import com.inverter.jakson.config.StoreUserDetailsConfig;
import com.inverter.jakson.dto.StoreUserDetailsDto;


@Repository
public class StoreUserDetailsDao extends AbstractTransactionalDao {
	
	@Autowired
	private StoreUserDetailsConfig storeUserDetailsConfig;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StoreUserDetailsDao.class);

//	public int checkUserExistence(StoreUserDetailsDto storeUserDetailsDto) {
//		
//		LOGGER.debug("Request received in DAo - checkUserExistence"+storeUserDetailsDto.getJkusername());
//		
//		Map<String,Object> parameters = new HashMap<String, Object>();
//		parameters.put("username", storeUserDetailsDto.getJkusername());
//		
//		LOGGER.debug("Executing Query"+ storeUserDetailsConfig.getCheckUserExistenceSql()+ "parameters are"+ parameters.get("user_email"));
//		int userExistenceStatus= getJdbcTemplate().queryForObject(storeUserDetailsConfig.getCheckUserExistenceSql(), parameters, Integer.class);
//		LOGGER.debug("Returning response "+ userExistenceStatus);
//		return userExistenceStatus;
//	}
	
	public int saveNewToken(String username, String authToken) {
		LOGGER.debug("Received request in DAO - savenewAuthToken");
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("username", username);
		parameters.put("authToken", authToken);
		
		LOGGER.debug("Executing Query"+storeUserDetailsConfig.getSaveNewUserSql()+"parameters are "+parameters.get("username"));
		int addNewUser;
		try {
			addNewUser = getJdbcTemplate().update(storeUserDetailsConfig.getSaveNewUserSql(), parameters);
		} catch (DataAccessException e) {
			addNewUser=-1;
		}
		LOGGER.debug("Returning "+addNewUser);
		return addNewUser;
	}
}
