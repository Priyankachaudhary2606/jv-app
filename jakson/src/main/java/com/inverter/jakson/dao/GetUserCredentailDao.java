package com.inverter.jakson.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.inverter.jakson.common.AbstractTransactionalDao;
import com.inverter.jakson.config.GetUserCredentialConfig;
import com.inverter.jakson.dto.GetUserCredentialDto;



@Repository
public class GetUserCredentailDao extends AbstractTransactionalDao{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetUserCredentailDao.class);
	
	@Autowired
	private GetUserCredentialConfig getUserCredentialConfig;
	
	private static final GetUserDetailsRowmapper getUserDetail_RowMapper = new GetUserDetailsRowmapper();
	
	
	public GetUserCredentialDto getUserCredentials(String jkusername) {


		try {
			LOGGER.debug("Inside try block, to execute query to get user details corresponding to"+jkusername);
			Map<String, Object> parameters = new HashMap<String, Object>();
			LOGGER.debug("Inserting the jakson username");
			parameters.put("jkusername", jkusername);
			LOGGER.debug("Execute query to get user details");
			return getJdbcTemplate().queryForObject(getUserCredentialConfig.getUserCredential(), parameters, getUserDetail_RowMapper);
			

		} catch (Exception e) {

			LOGGER.error("CATCHING -- Exception handled while getting user details in DAO method");
			LOGGER.error("Exception is " + e);
			LOGGER.error("Returning null");
			return null;

		}

	}

	
	private static class GetUserDetailsRowmapper implements RowMapper<GetUserCredentialDto> {
		
	
		public GetUserCredentialDto mapRow(ResultSet rs, int rownum) throws SQLException {
			
			String jkusername = rs.getString("username");
			String jkpassword = rs.getString("password");
			String authToken = rs.getString("auth_token");
			
			return new GetUserCredentialDto(jkusername, jkpassword, authToken);
		}
		
	}
}



