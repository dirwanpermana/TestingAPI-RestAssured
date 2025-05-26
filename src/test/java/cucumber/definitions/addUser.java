package cucumber.definitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import io.restassured.module.jsv.JsonSchemaValidator;

public class addUser {
    private String baseUrl;
    private Response response;
    private static String token;

    @Given("The base url in this feature is {string}")
    public void setBaseUrl(String url) {
        baseUrl = url;
    }

    @When("Send a http {string} request to {string} with body:")
    public void sendRequestWithBody(String method, String endpoint, String requestBody) {
        var requestSpec = given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .body(requestBody);

        if (token != null && !token.isEmpty()) {
            requestSpec.header("Authorization", "Bearer " + token);
        }

        switch (method.toUpperCase()) {
            case "POST":
                response = requestSpec.when().post(endpoint);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported method: " + method);
        }
    }

    @Then("The response status must be {int}")
    public void validateResponseStatus(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("The response schema should be match with schema {string}")
    public void validateJsonSchema(String schemaPath) {
        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
    }

    @Then("Save the token from the response to local storage")
    public void saveTokenFromResponse() {
        token = response.jsonPath().getString("token");
        if (token != null && !token.isEmpty()) {
            System.out.println("Token saved: " + token);
        } else {
            System.out.println("Token not found in response!");
        }
    }

    public static String getToken() {
        return token;
    }
}
