package com.inverter.jakson.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties.Jwt;
import org.springframework.stereotype.Service;

import com.inverter.jakson.dao.GetUserCredentailDao;
import com.inverter.jakson.dto.GetUserCredentialDto;

@Service
public class GetUserCredentialsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetUserCredentialsService.class);
	
	@Autowired
	private GetUserCredentailDao getUserCredentailDao;
	
	public GetUserCredentialDto getUserCredentials(String jkusername) {
		//getUserCredentials
		
		return getUserCredentailDao.getUserCredentials(jkusername);
	}
}
