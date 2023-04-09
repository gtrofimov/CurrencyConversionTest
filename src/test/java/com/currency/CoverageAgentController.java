package com.currency;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CoverageAgentController {
	
	public Response getStatus(String url) {
		Response responseStatus = RestAssured.given()
				.log()
				.all()
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.baseUri(url)
				.get("/status");
		
		// debug	
		System.out.println("The status received: " + responseStatus.statusLine());
		System.out.println("The body received: " + responseStatus.getBody().asString());
		return responseStatus;
	}
	
	@SuppressWarnings("unchecked")
	public void startTestCase(String url, String test, String testCase) {
		
		// start test case
		JSONObject requestParams = new JSONObject();
		requestParams.put("test", test );
		requestParams.put("testCase", testCase);
		
		Response responseStartTestCase = RestAssured.given()
			.log()
			.all()
			.header("Content-Type", "application/json")
			.header("Accept", "application/json")
			.baseUri(url)
			.body(requestParams.toJSONString())
			.post("/test/start");
		
		System.out.println("The status received: " + responseStartTestCase.statusLine());
		System.out.println("The body received: " + responseStartTestCase.getBody().asString());
	}
	
	@SuppressWarnings("unchecked")
	public void stopTest(String url, String testCase) {
		
		// stop test
		JSONObject requestParams = new JSONObject();
		requestParams.put("testCase", testCase);
		
		Response responseStopTest = RestAssured.given()
				.log()
				.all()
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.baseUri(url)
				.body(requestParams.toJSONString())
				.post("/test/stop");
			
			System.out.println("The status received: " + responseStopTest.statusLine());
			System.out.println("The body received: " + responseStopTest.getBody().asString());
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
		System.out.println("The body received: " + responseStopSession.getBody().asString());
	}
	
	public void getCoverage(String url, String port, String sessionId) {
		final String fileName = sessionId + "_" + port + ".zip";
		
		byte[] responseGetCoverage = RestAssured.given()
				.log()
				.all()
				.header("Content-Type", "application/json")
				.header("Accept", "application/octet-stream")
				.baseUri(url)
				.basePath("/coverage/{id}")
				.pathParam("id", sessionId)
				.get().asByteArray();
		
		try {
			OutputStream outStream = new FileOutputStream(fileName);
			outStream.write(responseGetCoverage);
			outStream.close();
			System.out.println("Downlaoded File: " + fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
