package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import util.configReader;

public class Authentication {

	String baseURI;
	String authEndPoint;
	String authBodyFilePath;
	String headerContentType;
	static long responseTime;
    public static String bearerToken;
	
    
    public Authentication() {
		baseURI = configReader.getProperty("baseURI");
		authEndPoint = configReader.getProperty("authEndPoint");
		authBodyFilePath = "src\\main\\java\\data\\authBody.json";
		headerContentType = configReader.getProperty("Content-Type");

	}

	public static boolean compareResponseTime() {

		boolean withinRange = false;

		if (responseTime <=3000) {
			System.out.println("response Time is within range!");
			withinRange = true;
		} else {
			System.out.println("Response TIme is Not in range!");
			withinRange = false;
		}
		return withinRange;

	}


	public String generateBearerToken() {

		/*
		 * given: all input details ->
		 * (basseURI,Headers,Authorization,payload/body,QueryParameters) When: submit
		 * api requests-> HttpMethod (Endpoint/resource) THen: validate reposnse->
		 * (Status code,Headers, responseTime,payload/body) Response response =
		 * get("/lotto"); String body = response.getBody().asString(); String
		 * headerValue = response.getHeader("headerName"); String cookieValue =
		 * response.getCookie("cookieName");
		 */
		Response response = 
				given().baseUri(baseURI).header("Content-Type", headerContentType)
				.body(new File(authBodyFilePath))
//				.log().all()
				.when()
				.post(authEndPoint).
                 then()
//                 .log().all()
                 .extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("status code:" + statusCode);
		Assert.assertEquals(statusCode, 201, "Status code NOT matching!");

	    responseTime = response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response Time :" + responseTime);
		Assert.assertEquals(compareResponseTime(), true);

//	String contentType= response.getHeader("Content-Type");
	String contentType= response.contentType();
	System.out.println("response Content-Type :" +  contentType);
	Assert.assertEquals( contentType, headerContentType);
	   
	String responseBody= response.getBody().asString();
	System.out.println("response body" + responseBody);
	
	    JsonPath jp = new JsonPath(responseBody);
    bearerToken = jp.getString("access_token");
    System.out.println("bearertoken:" + bearerToken);
    return bearerToken;
  
	
	}
}
