package cucumber.definitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import apiengine.Endpoints;
import helper.ConfigManager;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import junit.framework.Assert;

public class getObjectDefinition extends Endpoints {
    public static String baseUrl;
    public static Response response;
    public static String token;
    public static String objectId = "175";
    
    @Given("The base url in this feature is {string}")
    public void the_base_url_in_this_feature_is(String url) {
        baseUrl = url;
        System.out.println("Base URL is: " + baseUrl);
    }

    @When("Send user to login with body:")
    public void send_user_to_login_with_body(String body) {
        response = login(body);
    }

    @When("Send a request to get all object")
    public void send_request_to_get_all_object() {
        response = getAllObject(helper.ConfigManager.getToken());
    }

    // @When("Send a request to get all object")
    // public void send_request_to_get_all_object() {
    // response = getAllObject(ConfigManager.getToken());
    // // Ambil ID object pertama dari list
    // objectId = response.jsonPath().getString("data[0].id");
    // System.out.println("Fetched objectId for update: " + objectId);
    // }

    @Then("The response status must be {int}")
    public void the_response_status_must_be(Integer statusCode) {
        Assert.assertEquals(200, response.getStatusCode());

    }

    @Then("Save the token from the response to local storage")
    public void save_the_token_from_the_response_to_local_storage() {
    String token = response.jsonPath().getString("token");
    assertNotNull("Token not found!", token);
    helper.ConfigManager.setToken(token); // simpan ke runtime
}

    @Given("Make sure token in local storage not empty")
    public void make_sure_token_in_local_storage_not_empty() {
    String token = ConfigManager.getToken();  // ambil dari ConfigManager
    assertNotNull("Token should not be null", token);
    assertFalse("Token should not be empty", token.isEmpty());
}

    @When("Send a http {string} request to {string} with body:")
    public void send_a_http_request_to_with_body(String method, String endpoint, String body) {
        switch (method.toLowerCase()) {
            case "post":
                response = RestAssured.given()
                        .header("Content-Type", "application/json")
                        .body(body)
                        .when()
                        .post(baseUrl + endpoint);
                break;
            case "get":
                response = RestAssured.given()
                        .header("Authorization", "Bearer " + helper.ConfigManager.getToken())
                        .when()
                        .get(baseUrl + endpoint);
                break;
        }
    }

    @Then("The response schema should be match with schema {string}")
    public void the_response_schema_should_be_match_with_schema(String schemaName) {
    }

    @Then("The response body should contain {string} with value {string}")
    public void the_response_body_should_contain_with_value(String key, String expectedValue) {
        String actualValue = response.jsonPath().getString(key);
        assertEquals(expectedValue, actualValue);
    }

//     @Given("Make sure token not empty")
//     public void make_sure_token_not_empty() {
//         // token = ConfigManager.getToken();
//         String token = ConfigManager.getToken();
//         assertNotNull("Token should not be null", token);
//         assertFalse("Token should not be empty", token.isEmpty());
//     }

//     @When("Send update object with body:")
//     public void send_update_object_with_body(String body) {
//     String updateEndpoint = "/api/objects/" + objectId;
//     response = io.restassured.RestAssured.given()
//             .header("Content-Type", "application/json")
//             .header("Authorization", "Bearer " + helper.ConfigManager.getToken())
//             .body(body)
//             .log().all()
//             .when()
//             .put(updateEndpoint);
// }

//     @Then("name in the response must be {string}")
//     public void name_in_the_response_must_be(String expectedName) {
//         String actualName = response.jsonPath().getString("name");
//         assertEquals(expectedName, actualName);
//     }

    @When("Send create object with body:")
    public void send_create_object_with_body(String body) {
        response = addObject(helper.ConfigManager.getToken(), body);
    }

    @Then("Save the object ID from the response")
    public void save_the_object_id_from_the_response() {
        objectId = response.jsonPath().getString("id");
        assertNotNull("Object ID not found in response!", objectId);
        System.out.println("Saved object ID: " + objectId);
    }

    @Then("name in the response must be {string}")
    public void name_in_the_response_must_be(String expectedName) {
        List<String> names = response.jsonPath().getList("name");
        String actualName = names.get(0);
        assertEquals(expectedName, actualName);
    }
}
