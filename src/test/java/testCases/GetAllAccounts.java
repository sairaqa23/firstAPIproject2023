package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import util.configReader;

public class GetAllAccounts extends Authentication {

	String getAllAccountsEndPointFromConfig;
    String firstAccountId;
	public GetAllAccounts() {
		getAllAccountsEndPointFromConfig = configReader.getProperty("GetAllAccountsEndPoint");
	}

	@Test
	public void getAllAccounts() {

		/*
		 * given: all input details ->
		 * (basseURI,Headers,Authorization,payload/body,QueryParameters) When: submit
		 * api requests-> HttpMethod (Endpoint/resource) THen: validate reposnse->
		 */

		Response response =

		given()
				.baseUri(baseURI)
				.header("Content-Type", headerContentType)
				.header("Authorization", "Bearer " +  generateBearerToken()).
//				.log().all().
				
		when()
				.get(getAllAccountsEndPointFromConfig).
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
	 firstAccountId=jp.getString("records[0].account_id");
	 System.out.println("first acciunt:" + firstAccountId);
	 if(firstAccountId !=null) {
		 System.out.println("First Account is NOT null!");
	 }
	 else {
		 System.out.println("First account is null!");
	 }
	
	}

}
