package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import util.configReader;

public class GetAccountDetails extends Authentication {

	String GetAllAccountDetailsEndPointFromConfig;
   
	
    
    public GetAccountDetails() {
		GetAllAccountDetailsEndPointFromConfig = configReader.getProperty("GetAllAccountDetailsEndPoint");
	}

	@Test
	public void getAccountDetails() {

		/*
		 * given: all input details ->
		 * (basseURI,Headers,Authorization,payload/body,QueryParameters) When: submit
		 * api requests-> HttpMethod (Endpoint/resource) THen: validate reposnse->
		 */

		Response response =

		   given()
				.baseUri(baseURI)
				.header("Content-Type", headerContentType)
				.auth().preemptive().basic("demo1@codefios.com","abc123")
				.queryParam("account_id", "691").
//		        .log().all().
				
			when()
				.get(GetAllAccountDetailsEndPointFromConfig).
		     then()
//			    .log().all()
			     .extract().response();

	
		int statusCode = response.getStatusCode();
		System.out.println("status code:" + statusCode);
		Assert.assertEquals(statusCode, 200, "Status code NOT matching!");
		
		String contentType= response.contentType();
		System.out.println("response Content-Type :" +  contentType);
		Assert.assertEquals( contentType, headerContentType);
		
		String responseBody= response.getBody().asString();
		System.out.println("response body" + responseBody);
		
	    JsonPath jp = new JsonPath(responseBody);
	 String account_Name =jp.getString("account_name");
	   System.out.println("account name:" + account_Name);
		Assert.assertEquals( account_Name, "MD Techfios account 99999", "Account names are Not matching!");
	 
	
		 String account_Number =jp.getString("account_number");
		   System.out.println("account number:" + account_Number);
			Assert.assertEquals( account_Number, "999999" , "Account numbers are Not matching!");
			
			
			
			
			
			
			
	 
	}

}
