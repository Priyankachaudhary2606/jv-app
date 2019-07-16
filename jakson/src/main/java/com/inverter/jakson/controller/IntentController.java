package com.inverter.jakson.controller;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
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
	
//	@ForIntent("store user detail")
//	public ActionResponse saveUserDetails(ActionRequest request) {
//		ResponseBuilder responseBuilder = getResponseBuilder(request);
//		StoreUserDetailsDto storeUserDetailsDto = new StoreUserDetailsDto("hemant.sapra@jakson.com","jakson123","Hemant", "hemant@gmail.com",852752465);
//		LOGGER.debug(storeUserDetailsDto.getJkusername());
//		saveUserMessage = storeUserDetailsService.addUser(storeUserDetailsDto);
//		LOGGER.debug(saveUserMessage);
//		responseBuilder.add(saveUserMessage);
//		return responseBuilder.build();
//	}
	
	@ForIntent("PV Voltage Status")
	public ActionResponse getBatteryVoltage(ActionRequest request) {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
		String accessToken=request.getUser().getAccessToken();
		String username = jwtTokenDecode(accessToken);
		String name_of_unit = "ESS Device 62";
		String pV_Voltage = getInverterDataService.getBatteryVoltage(username, name_of_unit);
		responseBuilder.add("The PV Voltage of your inverter is "+ pV_Voltage);
		return responseBuilder.build();
	}
	
	
	
	public String jwtTokenDecode(String jwtToken ) {
		System.out.println("------------ Decode JWT ------------");
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
        System.out.println("JWT Header : " + header);


        System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        System.out.println("JWT Body : "+body);     
        
        JSONObject tokenBody = new JSONObject(body);
		String userName = tokenBody.getString("user_name");
		return userName;
	}
}
