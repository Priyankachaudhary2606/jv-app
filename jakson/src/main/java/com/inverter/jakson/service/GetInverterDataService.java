package com.inverter.jakson.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonValue;
import com.inverter.jakson.dto.GetUserCredentialDto;

@Service
public class GetInverterDataService {
	
	String unit_key;
	Integer value = null;
	String unit_of_measure = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(GetInverterDataService.class);
	
	@Autowired
	private GetUserCredentialsService userCredentialsService;
	
	public String getBatteryVoltage() {
		
		LOGGER.debug("Request recieved in service to get battery voltage");
		String jkusername = "hemant.sapra@jakson.com";
		//getUserCredentials
		GetUserCredentialDto getUserCredentialDto = userCredentialsService.getUserCredentials(jkusername);
		//getAuthToken
		String authtoken = getAuthToken(getUserCredentialDto.getJkusername(),getUserCredentialDto.getJkpassword());
		//get all site data having all units
		String allSiteData = getAllSitesData(authtoken);
		String siteKey = "21c5t96537";
		JSONObject sites = new JSONObject(allSiteData);
		JSONArray unitkey = sites.getJSONArray("units");
		for(int i = 0; i<unitkey.length(); i++ ) {
			JSONObject jb = unitkey.getJSONObject(i);
			String site_key = jb.getString("site_key");
			if(site_key.equals(siteKey)) {
				unit_key = jb.getString("unit_key");
				System.out.println("To get the unit keys "+unit_key);
				break;
			}
            
			
		}
		
		//get information of inverter for a particular parameter
		String inverterData = dataForInverter(authtoken, unit_key);
		LOGGER.debug("Returned Inverter data is "+inverterData);
		JSONObject inverterDataJson = new JSONObject(inverterData);
		LOGGER.debug("The inverter data ");
		JSONObject dataForUnitKey = inverterDataJson.getJSONObject("f92956te22");
		JSONArray dataForParameters = dataForUnitKey.getJSONArray("data");
		
		for(int i = 0; i<dataForParameters.length(); i++ ) {
			JSONObject jb = dataForParameters.getJSONObject(i);
			if (i == dataForParameters.length()-1) {
				value = jb.getInt("value");
				 unit_of_measure = jb.getString("unit_of_measure");
				 System.out.println("To get the data for parameter "+value+unit_of_measure);
			}
            
			
		}
		//response for voice
		return value+unit_of_measure;
	}
	
	public String getAuthToken(String username, String password) {
		LOGGER.debug("Service to get authtoken for"+username+password);
		try {
				
			String payload = "{" +
	                "\"email\": \"hitaksh@asun.co.in\", " +
	                "\"password\": \"asun1234\"" +
	                "}";
	        StringEntity entity = new StringEntity(payload,
	                ContentType.APPLICATION_JSON);

	        HttpClient httpClient = HttpClientBuilder.create().build();
	        HttpPost request = new HttpPost("https://integrationsapi.trackso.in/integrations/v1/api_sign_in");
	        request.setEntity(entity);
	        request.addHeader("Cache-Control", "no-cache");
	        request.addHeader("Accept", "*/*");
	        request.addHeader("X-API-Key", "f732dfdbd0aed62727f958cccca9ec3a5cb13eda");       
	        HttpResponse response = httpClient.execute(request);
	        HttpEntity responseBody = response.getEntity();
	        String responseString = EntityUtils.toString(responseBody, "UTF-8");
	        System.out.println(responseString);
	        JSONObject webhookJson = new JSONObject(responseString);
	        String authToken = webhookJson.getString("auth_token");
	        LOGGER.debug("The http response for sign in is "+response.getStatusLine().getStatusCode()+response.toString()+responseString+authToken);
	        return authToken;
		}
		catch(Exception e){
			LOGGER.error("Exception handled with hitting the sign-in API "+e);
		}
		return null;
	}
	
	public String getAllSitesData(String authtoken) {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet("https://integrationsapi.trackso.in/integrations/v1/units?per=3&page=1");
			request.addHeader("Cache-Control", "no-cache");
	        request.addHeader("x-auth-token", authtoken);
	        request.addHeader("Accept", "*/*");
	        request.addHeader("X-API-Key", "f732dfdbd0aed62727f958cccca9ec3a5cb13eda");  
	        try {
				HttpResponse response = httpClient.execute(request);
				HttpEntity responseBody = response.getEntity();
		        String responseString = EntityUtils.toString(responseBody, "UTF-8");
		        System.out.println(responseString);
		        LOGGER.debug("The http response for get all site data is "+response.getStatusLine().getStatusCode()+response.toString()+responseString+authtoken);
		        return responseString;
	        } catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error("Exception handled in dataForInverter"+e);
				return null;
			} 
	}
	
	
	public String dataForInverter(String authToken, String unitkey) {
		String payload = "{\r\n" + 
				"\"units\":{\"f92956te22\": [\"Daily Energy\",\"Total Energy\"]},\r\n" + 
				"\"from\":1554057000,\r\n" + 
				"\"to\":1554143400\r\n" + 
				"}";
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://integrationsapi.trackso.in/integrations/v1/unit_data");
        request.setEntity(entity);
        request.addHeader("Cache-Control", "no-cache");
        request.addHeader("x-auth-token", authToken);
        request.addHeader("Accept", "*/*");
        request.addHeader("X-API-Key", "f732dfdbd0aed62727f958cccca9ec3a5cb13eda");       
        try {
			HttpResponse response = httpClient.execute(request);
			HttpEntity responseBody = response.getEntity();
	        String responseString = EntityUtils.toString(responseBody, "UTF-8");
	        System.out.println(responseString);
	        LOGGER.debug("The http response for unit data is "+response.getStatusLine().getStatusCode()+response.toString()+responseString+authToken);
	        return responseString;
        } catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("Exception handled in dataForInverter"+e);
			e.printStackTrace();
		} 
		
        return null;
		
	}
	
	
	
}
