package cucumber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ObjectSteps {
    
    private static String baseUrl;
    private static String token;
    private Response response;
    private RequestSpecification requestSpec;

    @Given("The base url in this feature is {string}")
    public void setBaseUrl(String url) {
        baseUrl = url;
        RestAssured.baseURI = baseUrl;  // Set base URL untuk semua request
    }

    @When("Send a http {string} request to {string} with body:")
    public void sendHttpRequest(String method, String endpoint, String body) {
        requestSpec = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(body);

        response = requestSpec.request(method, endpoint);
    }

    @Then("The response status must be {int}")
    public void validateStatusCode(int expectedStatusCode) {
        assertThat("Status code tidak sesuai", 
            response.getStatusCode(), 
            equalTo(expectedStatusCode)
        );
    }

    @And("The response schema should be match with schema {string}")
    public void validateSchema(String schemaPath) {
        response.then().assertThat().body(
            JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath)
        );
    }

    @And("Save the token from the response to local storage")
    public void extractTokenFromResponse() {
        token = response.jsonPath().getString("token");
        assertThat("Token tidak ditemukan dalam response", token, notNullValue());
    }

    @And("The response body should contain {string} with value {string}")
    public void validateResponseBodyField(String fieldPath, String expectedValue) {
        String actualValue = response.jsonPath().getString(fieldPath);
        assertThat(
            "Nilai field '" + fieldPath + "' tidak sesuai",
            actualValue, 
            equalToIgnoringCase(expectedValue)
        );
    }

    @Then("Full name in the response must be {string}")
    public void Full_name_in_the_response_must_be(String s) {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("name in the response must be {string}")
    public void name_in_the_response_must_be(String s) {
        // Write code here that turns the phrase above into concrete actions
    }
}