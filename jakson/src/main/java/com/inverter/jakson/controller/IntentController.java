package com.inverter.jakson.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;
import com.inverter.jakson.config.StoreUserDetailsConfig;
import com.inverter.jakson.dto.StoreUserDetailsDto;
import com.inverter.jakson.service.GetInverterDataService;
import com.inverter.jakson.service.StoreUserDetailsService;

@Component
public class IntentController extends DialogflowApp{
	
	private String saveUserMessage;
	
	@Autowired
	private StoreUserDetailsService storeUserDetailsService;
	
	@Autowired
	private GetInverterDataService getInverterDataService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IntentController.class);
	
	@ForIntent("store user detail")
	public ActionResponse saveUserDetails(ActionRequest request) {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		StoreUserDetailsDto storeUserDetailsDto = new StoreUserDetailsDto("hemant.sapra@jakson.com","jakson123","Hemant", "hemant@gmail.com",852752465);
		LOGGER.debug(storeUserDetailsDto.getJkusername());
		String my = getInverterDataService.getBatteryVoltage();
		saveUserMessage = storeUserDetailsService.addUser(storeUserDetailsDto);
		LOGGER.debug(saveUserMessage);
		responseBuilder.add(saveUserMessage+my);
		return responseBuilder.build();
	}
	
	@ForIntent("get battery voltage")
	public ActionResponse getBatteryVoltage(ActionRequest request) {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		
		responseBuilder.add(saveUserMessage);
		return responseBuilder.build();
	}
}
