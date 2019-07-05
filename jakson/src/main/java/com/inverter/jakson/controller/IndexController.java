package com.inverter.jakson.controller;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

	@Autowired
	private IntentController intentController;
	
	@RequestMapping(value = "/voice", method = RequestMethod.POST, produces = {"application/json"})
	public String webhookFunction(@RequestBody String body, @RequestHeader Map<String, String> headers)  {
		try {
			return intentController.handleRequest(body, headers).get();
		} catch (InterruptedException e) {
			return handleError(e);
		} catch (ExecutionException e) {
			return handleError(e);
		}
	}
	
	private String handleError(Exception e) {
        return "Error occured while handling the intent request- " + e.getMessage();
    }
}
