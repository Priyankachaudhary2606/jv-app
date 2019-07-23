package com.inverter.jakson.service;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inverter.jakson.dao.StoreUserDetailsDao;
import com.inverter.jakson.dto.GetUserCredentialDto;

@Service
public class GetInverterDataService {
	
	String unit_key = null;
	Double value = null;
	String unit_of_measure = null;
	String allSiteData = null;
	String unitNames = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(GetInverterDataService.class);
	
	@Autowired
	private GetUserCredentialsService userCredentialsService;
	
	@Autowired
	private StoreUserDetailsDao storeUserDetailsDao;
	
	public String getResponseForVoice(String jkusername, String name_of_unit, String parameter) {
		String response = getBatteryVoltage(jkusername, name_of_unit, parameter);
		String responseForVoice = null;
		if(response != null && !response.isEmpty()) {
			if(response.equals("invalid username or password")) {
				responseForVoice = "Please link your account again. You may have logged out.";
			}
			else {
				
				responseForVoice = "The "+parameter+" of your inverter is "+response+" . What else would you like to know?";
			}
		}
		else {
			responseForVoice = "I could not get the "+parameter+"of your inverter. Please check if your inverter is active";
		}
		return responseForVoice;
	}
	
	public String getBatteryVoltage(String jkusername, String name_of_unit, String parameter) {
		
		LOGGER.debug("Request recieved in service to get battery voltage");
		LOGGER.debug("Getting user credentials from database");
		GetUserCredentialDto getUserCredentialDto = userCredentialsService.getUserCredentials(jkusername);
		LOGGER.debug("Setting auth token received from database");
		String authtoken = getUserCredentialDto.getAuthToken();
	
		LOGGER.debug("Using Auth token, hit the request to get site data");
		allSiteData = getAllSitesData(authtoken);	
		LOGGER.debug("Response received from API is"+ allSiteData);
		JSONObject sites = new JSONObject(allSiteData);
		LOGGER.debug("Checking if response has any unit data");
		if(sites.has("units")) {
			LOGGER.debug("When unit is available in site");
			JSONArray unitkey = sites.getJSONArray("units");
			for(int i = 0; i<unitkey.length(); i++ ) {
				JSONObject jb = unitkey.getJSONObject(i);
				unitNames = jb.getString("name");
				if(unitNames.equals(name_of_unit)) {
					unit_key = jb.getString("unit_key");
					System.out.println("To get the unit keys "+unit_key);
					break;
				}
			}
		}
		
		else if(sites.has("error")){
			LOGGER.debug("When in response of API, an error message is encountered");
			LOGGER.debug("Requesting to authenticate user again & get a new auth token");
			authtoken = getAuthToken(getUserCredentialDto.getJkusername(),getUserCredentialDto.getJkpassword());
			
			if(authtoken.equals("invalid username or password")) {
				LOGGER.debug("No auth token has been received");
				return "invalid username or password";
			}
			else {
				LOGGER.debug("Received a new auth token");
				LOGGER.debug("Save new auth token for user");
				int saveAuthToken = storeUserDetailsDao.saveNewToken(getUserCredentialDto.getJkusername(), authtoken);
				
				allSiteData = getAllSitesData(authtoken);
				if(sites.has("units")) {
					JSONArray unitkey = sites.getJSONArray("units");
					for(int i = 0; i<unitkey.length(); i++ ) {
						JSONObject jb = unitkey.getJSONObject(i);
						String unitNames = jb.getString("name");
						if(unitNames.equals(name_of_unit)) {
							unit_key = jb.getString("unit_key");
							System.out.println("To get the unit keys "+unit_key);
							break;
						}
					}
				}
				
				else {
					return null;
				}

			}
			
		}
		
		else {
			return null;
		}
		

		
		//get information of inverter for a particular parameter
		if(unit_key !=null || !unit_key.isEmpty()) {
			String inverterData = dataForInverter(authtoken, unit_key, parameter);
			LOGGER.debug("Returned Inverter data is "+inverterData);
			JSONObject inverterDataJson = new JSONObject(inverterData);
			LOGGER.debug("The inverter data is"+ unit_key);
				JSONObject dataForUnitKey = inverterDataJson.getJSONObject(unit_key);
				JSONArray dataForParameters = dataForUnitKey.getJSONArray("data");
				LOGGER.debug("The inverter"+dataForUnitKey+" data is"+ dataForParameters.length());
				
				for(int i = 0; i<dataForParameters.length(); i++ ) {					
					JSONObject jb = dataForParameters.getJSONObject(i);
					if (i == dataForParameters.length()-1) {
						value = jb.getDouble("value");
						 unit_of_measure = jb.getString("unit_of_measure");
						 System.out.println("To get the data for parameter "+value+unit_of_measure);
					}				
				}
				LOGGER.debug("After retrieving the data returning it to voice "+ value+unit_of_measure);
				return value+unit_of_measure;
			
	
		}

		//response for voice
		return value+unit_of_measure;
	}
	
	public String getAuthToken(String username, String password) {
		LOGGER.debug("Service to get authtoken for"+username+password);
		try {
				
			String payload = "{" +
	                "\"email\": \""+username+"\", " +
	                "\"password\": \""+password+"\"" +
	                "}";
			
			LOGGER.debug("Payload is ------- "+payload);
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
	        if(webhookJson.has("auth_token")) {
	        	String authToken = webhookJson.getString("auth_token");
		        LOGGER.debug("The http response for sign in is "+response.getStatusLine().getStatusCode()+response.toString()+responseString+authToken);
		        return authToken;
	        }
	        else {
	        	return "invalid username or password";
	        }
	        
		}
		catch(Exception e){
			LOGGER.error("Exception handled with hitting the sign-in API "+e);
		}
		return null;
	}
	
	public String getAllSitesData(String authtoken) {
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet("https://api.trackso.in/integrations/v1/units?per=3&page=1");
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
	
	
	public String dataForInverter(String authToken, String unitkey, String parameter) {
		String payload = "{\r\n" + 
				"\"units\":{\""+unitkey+"\": [\""+parameter+"\"]},\r\n" + 
				"\"from\":1562229000,\r\n" + 
				"\"to\":1562231100\r\n" + 
				"}";
		
		LOGGER.debug("Payload is --------"+ payload+"---------unitkey -----"+authToken);
        StringEntity entity = new StringEntity(payload,
                ContentType.APPLICATION_JSON);

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("https://api.trackso.in/integrations/v1/unit_data");
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
