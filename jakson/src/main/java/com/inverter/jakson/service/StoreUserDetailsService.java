//package com.inverter.jakson.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.inverter.jakson.dao.StoreUserDetailsDao;
//import com.inverter.jakson.dto.StoreUserDetailsDto;
//
//@Service
//public class StoreUserDetailsService {
//	
//	@Autowired
//	private StoreUserDetailsDao storeUserDetailsDao;
//	
//	private static final Logger LOGGER = LoggerFactory.getLogger(StoreUserDetailsService.class);
//	
//	String addUserResponse;
//	int checkUserExistenceStatus;
//	int saveUserStatus;
//	
//	
//	
//	public String addUser(StoreUserDetailsDto storeUserDetailsDto) {
//		LOGGER.debug("Request received in Store User Details Service");
//		LOGGER.debug(storeUserDetailsDto.getJkusername());
//		checkUserExistenceStatus = this.checkUserExistence(storeUserDetailsDto);
//		if(checkUserExistenceStatus==0) {
//			saveUserStatus = this.saveNewUser(storeUserDetailsDto);
//			LOGGER.debug("Save user status is"+saveUserStatus);
//			if(saveUserStatus==-1) {
//				addUserResponse = "Linking process couldn't complete, please say \'link me\' to try again";
//				return addUserResponse;
//			}
//			else {
//				addUserResponse = "Hey fella, tell me what can I do for you?";
//				return addUserResponse;
//			}
//		}
//		else {
//			addUserResponse="Hey, tell me what can I do for you?";
//			return addUserResponse;
//		}
//		
//	}
//	
//	
//	public int saveNewUser(StoreUserDetailsDto storeUserDetailsDto) {
//		saveUserStatus = storeUserDetailsDao.saveNewUser(storeUserDetailsDto);
//		return saveUserStatus;
//		
//	}
//	
//	
//	
//	public int checkUserExistence(StoreUserDetailsDto storeUserDetailsDto) {
//		checkUserExistenceStatus = storeUserDetailsDao.checkUserExistence(storeUserDetailsDto);
//		return checkUserExistenceStatus;
//	}
//	
//
//}
