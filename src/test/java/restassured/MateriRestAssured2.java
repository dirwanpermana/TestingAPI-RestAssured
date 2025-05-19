package restassured;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
public class MateriRestAssured2 {
String token;
    
    @BeforeSuite
    public void setUp(){
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        String requestBody = "{\n" + //
                        "  \"email\": \"albertjuntak13@gmail.com\",\n" + //
                        "  \"password\": \"afteroffice123\"\n" + //
                        "}";
        // Send POST request to login endpoint
        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .when()
                .post("/webhook/employee/login");
        // Print the response
        // System.out.println("Response: " + response.asPrettyString()); 
        token = response.jsonPath().getString("[0].token");  
        System.out.println("Token: " + token);      
    }

    @Test()
    public void testGetEmployee(){
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";
        // Create Get Employee request
        // Send GET request to employee endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("/webhook/employee/get");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());
        // Validate the response
        // Assert.assertEquals(response.getStatusCode(), 200);
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        assert response.jsonPath().getString("[0].email").equals("albertjuntak13@gmail.com") : "Expected email albertjuntak13@gmail.com but got " + response.jsonPath().getString("[0].email");
        assert response.jsonPath().getString("[0].full_name").equals("Albert Simanjuntak") : "Expected name Albert Juntak but got " + response.jsonPath().getString("[0].full_name");
        assert response.jsonPath().getString("[0].department").equals("manager") : "Expected department manager but got " + response.jsonPath().getString("[0].department");
    }

    @Test(priority = 2)
    public void testUpdateEmployee(){
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";
        // Create Update Employee request
        String bodyUpdate = "{\n" + //
                        "    \"email\": \"albertjuntak13@gmail.com\",\n" + //
                        "    \"full_name\": \"Albert Simanjuntak\",\n" + //
                        "    \"department\": \"science\",\n" + //
                        "    \"title\": \"Biology\",\n" + //
                        "    \"password\" : \"afteroffice123\"\n" + //
                        "}";
                         
        // Send POST request to employee endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(bodyUpdate)
                .log().all()
                .when()
                .put("/webhook/employee/update");

        System.out.println("Response: " + response.asPrettyString());
    }


    @Test
    public void testGetAllEmployee(){
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";
        // Create Get All Employee request
        // Send GET request to employee endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .log().all()
                .when()
                .get("/webhook/employee/get_all");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());
    }

    @Test
    public void getEmployeeInvalidToken(){
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";
        // Create Get Employee request
        // Send GET request to employee endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token + "invalid")
                .log().all()
                .when()
                .get("/webhook/employee/get");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());

        // Validate the response
        assert response.getStatusCode() == 403 : "Expected status code 403 but got " + response.getStatusCode();
        
    }

    @AfterSuite
    public void tearDown(){
        // Clean up resources or perform any necessary actions after the tests
        System.out.println("All tests completed.");

         /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";
        // Create Delete Employee request
        // Send DELETE request to employee endpoint
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .delete("/webhook/employee/delete");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());
    }
}
