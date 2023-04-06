package com.currency;

import java.lang.reflect.Method;

import org.json.simple.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;


public class testCurrencyConversion {
	
	// http://localhost:8100/currency-converter/from/USD/to/INR/quantity/1000
	private static final String APP_HOST = "http://34.211.11.203";
	private static final String APP_PORT = "8100";
	private static final String COV_PORT = "8052";
	
	final static String from = "USD";
	final static String to = "INR";
	final static String quantity = "1000";

	@BeforeMethod
	public void beforeTest(Method method) {
		// set test name and test case name
		String testName = method.getName(); 
		String className = this.getClass().getName();
		
		// construct request body
		JSONObject requestParams = new JSONObject();
		requestParams.put("test", className );
		requestParams.put("testCase", testName);
		
		// start test case
		Response response = RestAssured.given()
			.log()
			.all()
			.header("Content-Type", "application/json")
			.header("Accept", "application/json")
			.baseUri(APP_HOST + ":" + COV_PORT)
			.body(requestParams.toJSONString())
			.post("/test/start");
		
		System.out.println("The status received: " + response.statusLine());
		
		Response responseStatus = RestAssured.given()
				.log()
				.all()
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.baseUri(APP_HOST + ":" + COV_PORT)
				.post("/status");
			
			System.out.println("The status received: " + responseStatus.statusLine());
	}
	
	@AfterMethod
	public void afterTest() {
		// set test name and test case name
		//String testName = method.getName(); 
		String className = this.getClass().getName();
		
		// construct request body
		JSONObject requestParams = new JSONObject();
		requestParams.put("test", className);
		//requestParams.put("testCase", testName);
		
		// start test case
		Response responseTestStart = RestAssured.given()
			.log()
			.all()
			.header("Content-Type", "application/json")
			.header("Accept", "application/json")
			.baseUri(APP_HOST + ":" + COV_PORT)
			.body(requestParams.toJSONString())
			.post("/test/stop");
		
		System.out.println("The status received: " + responseTestStart.statusLine());
		
	}
	
	@Test
	public static void testConversion() {
		RestAssured.given()
			.log()
			.all()
			.baseUri(APP_HOST + ":" + APP_PORT)
			.basePath("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
			.pathParam("from", from)
			.pathParam("to", to)
			.pathParam("quantity", quantity)
			.when()
				.get()
			.then()
				.assertThat().statusCode(200);
	}

}