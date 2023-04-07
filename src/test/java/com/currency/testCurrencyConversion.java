package com.currency;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.restassured.RestAssured;

public class testCurrencyConversion {

	// app info
	// http://localhost:8100/currency-converter/from/USD/to/INR/quantity/1000
	private static final String APP_HOST = "http://34.211.11.203";
	private static final String APP_PORT = "8100";

	// cov agent info
	CoverageAgentController obj = new CoverageAgentController();
	String COV_PORT[] = { "8051", "8052" };
	public HashMap<String, String> sessions = new HashMap<String, String>();

	// start tests
	@BeforeMethod
	public void beforeTest(Method method) {
		// set test name and test case name
		String testCase = method.getName();
		String test = this.getClass().getName();

		// call startTest method
		for (String port : COV_PORT) {
			String url = APP_HOST + ":" + port;
			obj.startTestCase(url, test, testCase);
			String sessionId = obj.getStatus(url).getBody().jsonPath().get("session");
			sessions.put(port, sessionId);
			System.out.println(sessions);
	
		}
	}

	// stop tests
	@AfterMethod
	public void afterTest() {

		// set test name
		String test = this.getClass().getName();

		// call startTest method
		for (String port : COV_PORT) {
			String url = APP_HOST + ":" + port;
			obj.stopTest(url, test);
			obj.getStatus(url);
		}
	}

	@AfterSuite
	public void afterSuite() {
		for (String port : COV_PORT) {
			String url = APP_HOST + ":" + port;
			obj.stopSession(url);
			obj.getStatus(url);
			String sessionId = sessions.get(port);
			obj.getCoverage(url, port, sessionId);
		}
	}
	@Test
	public static void testConversion() {
		// test params
		final String from = "USD";
		final String to = "INR";
		final String quantity = "1000";

		// run test
		RestAssured.given()
				.log().all()
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