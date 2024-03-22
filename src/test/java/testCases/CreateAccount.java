package testCases;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.configReader;

public class CreateAccount extends Authentication {

	String createAccountEndPointFromConfig;
	String createAccountBodyFilePath;
	String GetAllAccountDetailsEndPointFromConfig;
	String getAllAccountsEndPointFromConfig;
	String firstAccountId;

	public CreateAccount() {

		createAccountEndPointFromConfig = configReader.getProperty("createAccountEndPoint");
		createAccountBodyFilePath = "src\\main\\java\\data\\createAccountBody.json";
		GetAllAccountDetailsEndPointFromConfig = configReader.getProperty("GetAllAccountDetailsEndPoint");
		getAllAccountsEndPointFromConfig = configReader.getProperty("GetAllAccountsEndPoint");
	}

	@Test(priority = 1)
	public void createAccount() {

		Response response =

			given()
				.baseUri(baseURI)
				.header("Content-Type", headerContentType)
				.header("Authorization", "Bearer " + generateBearerToken())
				.body(new File(createAccountBodyFilePath)).
//				.log().all().

			when()
			   .post(createAccountEndPointFromConfig).
			 then()
//			   .log().all()
			   .extract().response();

		int statusCode = response.getStatusCode();
		System.out.println("status code:" + statusCode);
		Assert.assertEquals(statusCode, 201, "Status code NOT matching!");

		String contentType = response.contentType();
		System.out.println("response Content-Type :" + contentType);
		Assert.assertEquals(contentType, headerContentType);

		String responseBody = response.getBody().asString();
		System.out.println("response body" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		String successMessage = jp.getString("message");
		System.out.println(" success message:" + successMessage);
		Assert.assertEquals(successMessage, "Account created successfully.", "success messages are Not matching!");

	}

	@Test(priority = 2)
	public void getAllAccounts() {

		Response response =

			given()
			   .baseUri(baseURI)
			   .header("Content-Type", headerContentType)
			   .header("Authorization", "Bearer " + generateBearerToken()).
//				.log().all().

			when()
			     .get(getAllAccountsEndPointFromConfig).
			then()
//			   .log().all()
			   .extract().response();

	
		
		String responseBody = response.getBody().asString();
		System.out.println("response body" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		firstAccountId = jp.getString("records[0].account_id");
		System.out.println("first account:" + firstAccountId);

	}

	@Test(priority = 3)

	public void getAccountDetails() {

		Response response =

			given()
			    .baseUri(baseURI)
			    .header("Content-Type", headerContentType)
			    .auth().preemptive()
				.basic("demo1@codefios.com", "abc123")
				.queryParam("account_id",firstAccountId).
//				.log().all().

			when()
			     .get(GetAllAccountDetailsEndPointFromConfig).
			then()
//				 .log().all()
				 .extract().response();

		String responseBody = response.getBody().asString();
		System.out.println("response body" + responseBody);

		JsonPath jp = new JsonPath(responseBody);
		
		String actualAccountName = jp.getString("account_name");
		System.out.println(" Actual Account name:" + actualAccountName);
		

		String actualAccountNumber = jp.getString("account_number");
		System.out.println("Actual Account number:" +  actualAccountNumber);
		
		String actualAccountDescription = jp.getString("description");
		System.out.println(" Actual Account description:" + actualAccountDescription);
		
		String actualAccountBalance = jp.getString("balance");
		System.out.println("Actual Account Balance:" + actualAccountBalance );
		
		String actualAccountPerson = jp.getString("contact_person");
		System.out.println("Actual AccountPerson:" + actualAccountPerson);
		
		File expectedRequestBody = new File("createAccountBodyFilePath");
		JsonPath jp2 = new JsonPath(expectedRequestBody);
		
		
		String expectedAccountName = jp.getString("account_name");
		System.out.println("Expected Account name:" + expectedAccountName);
		

		String expectedAccountNumber = jp.getString("account_number");
		System.out.println("Expected Account number:" +  expectedAccountNumber);
		
		String expectedAccountDescription = jp.getString("description");
		System.out.println(" Expected Account description:" + expectedAccountDescription);
		
		String expectedAccountBalance = jp.getString("balance");
		System.out.println("Expected Account Balance:" + expectedAccountBalance );
		
		String expectedAccountPerson = jp.getString("contact_person");
		System.out.println("Expected Account Person:" + expectedAccountPerson);
		
		Assert.assertEquals(actualAccountName, expectedAccountName, "Accounts Names are NOT matching!");
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber, "Account Numbers are NOT matching!");
		Assert.assertEquals(actualAccountDescription,  expectedAccountDescription, "Account descriptions are NOT matching!");
		Assert.assertEquals(actualAccountBalance , expectedAccountBalance, "Accounts balances are NOT matching!");
		Assert.assertEquals(actualAccountPerson, expectedAccountPerson, "Accounts persons are NOT matching!");
	
	
	}
	
	

}
