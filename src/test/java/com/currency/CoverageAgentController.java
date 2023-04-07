package com.currency;

import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CoverageAgentController {
	
	public void getStatus(String url) {
		Response responseStatus = RestAssured.given()
				.log()
				.all()
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.baseUri(url)
				.get("/status");
			
		System.out.println("The status received: " + responseStatus.statusLine());
	}
	
	@SuppressWarnings("unchecked")
	public void startTest(String url, String test, String testCase) {
		
		// start test case
		JSONObject requestParams = new JSONObject();
		requestParams.put("test", test );
		requestParams.put("testCase", testCase);
		
		Response response = RestAssured.given()
			.log()
			.all()
			.header("Content-Type", "application/json")
			.header("Accept", "application/json")
			.baseUri(url)
			.body(requestParams.toJSONString())
			.post("/test/start");
		
		System.out.println("The status received: " + response.statusLine());
	}
	
	@SuppressWarnings("unchecked")
	public void stopTest(String url, String testCase) {
		
		// stop test
		JSONObject requestParams = new JSONObject();
		requestParams.put("testCase", testCase);
		
		Response responseTestStart = RestAssured.given()
				.log()
				.all()
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.baseUri(url)
				.body(requestParams.toJSONString())
				.post("/test/stop");
			
			System.out.println("The status received: " + responseTestStart.statusLine());
	}
	
	public void stopSession(String url) {
		Response responseStopSession = RestAssured.given()
				.log()
				.all()
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.baseUri(url)
				.get("/session/stop");
			
		System.out.println("The status received: " + responseStopSession.statusLine());
	}
}
