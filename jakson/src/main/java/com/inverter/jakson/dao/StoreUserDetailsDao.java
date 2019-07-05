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

	public int checkUserExistence(StoreUserDetailsDto storeUserDetailsDto) {
		
		LOGGER.debug("Request received in DAo - checkUserExistence"+storeUserDetailsDto.getJkusername());
		
		Map<String,Object> email = new HashMap<String, Object>();
		email.put("user_email", storeUserDetailsDto.getJkusername());
		LOGGER.debug("Executing Query"+ storeUserDetailsConfig.getCheckUserExistenceSql()+ "parameters are"+ email.get("user_email"));
		int userExistenceStatus= getJdbcTemplate().queryForObject(storeUserDetailsConfig.getCheckUserExistenceSql(), email, Integer.class);
		LOGGER.debug("Returning response "+ userExistenceStatus);
		return userExistenceStatus;
	}
	
	public int saveNewUser(StoreUserDetailsDto storeUserDetailsDto) {
		LOGGER.debug("Received request in DAO - savenewuser"+storeUserDetailsDto.getJkusername());
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("jkusername", storeUserDetailsDto.getJkusername());
		parameters.put("jkpassword", storeUserDetailsDto.getJkpassword());
		parameters.put("displayname", storeUserDetailsDto.getDisplayname());
		parameters.put("mobileno", storeUserDetailsDto.getMobileno());
		parameters.put("voiceid", storeUserDetailsDto.getVoiceid());
		LOGGER.debug("Executing Query"+storeUserDetailsConfig.getSaveNewUserSql()+"parameters are "+parameters.get("jkusername"));
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
