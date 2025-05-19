package restassured;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class MateriRestAssured {
String token;   // Variabel global untuk token login 
    
    @Test()
    public void testLogin(){
        /*
         * Define the base URL for the API
         * String baseUrl = "https://whitesmokehouse.com";
         */
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Create login request
        String requestBody = "{\n" + //
                        "  \"email\": \"dirwantea@gmail.com\",\n" + //
                        "  \"password\": \"Bintaro1!\"\n" + //
                        "}";
        // Mengirim permintaan POST ke endpoint login
        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()    // Logging seluruh request utk debuging
                .when()
                .post("/webhook/employee/login");
        
        // Mengambil token dari response untuk digunakan pada request berikutnya
        token = response.jsonPath().getString("[0].token");  
        System.out.println("Token: " + token);      
    }

    @Test(dependsOnMethods = "testLogin")
    public void testGetEmployee(){
         // Base URL untuk permintaan ini
        RestAssured.baseURI = "https://whitesmokehouse.com";

        // Mengirim permintaan GET ke endpoint get employee dengan token
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("/webhook/employee/get");

        // Menampilkan response ke console
        System.out.println("Response: " + response.asPrettyString());
        // Validate the response
        // Assert.assertEquals(response.getStatusCode(), 200);
        assert response.getStatusCode() == 200 : "Expected status code 200 but got " + response.getStatusCode();
        assert response.jsonPath().getString("[0].email").equals("albertjuntak13@gmail.com") : "Expected email albertjuntak13@gmail.com but got " + response.jsonPath().getString("[0].email");
        assert response.jsonPath().getString("[0].full_name").equals("Albert Simanjuntak") : "Expected name Albert Juntak but got " + response.jsonPath().getString("[0].full_name");
        assert response.jsonPath().getString("[0].department").equals("manager") : "Expected department manager but got " + response.jsonPath().getString("[0].department");
    }

    @Test(dependsOnMethods = "testLogin", priority = 2)
    public void testUpdateEmployee(){
       // Base URL untuk permintaan ini
        RestAssured.baseURI = "https://whitesmokehouse.com";
        // Membuat request body untuk update data karyawan
        String bodyUpdate = "{\n" + //
                        "    \"email\": \"albertjuntak13@gmail.com\",\n" + //
                        "    \"full_name\": \"Albert Simanjuntak\",\n" + //
                        "    \"department\": \"science\",\n" + //
                        "    \"title\": \"Biology\",\n" + //
                        "    \"password\" : \"afteroffice123\"\n" + //
                        "}";
                         
        // Mengirim permintaan PUT ke endpoint update employee
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(bodyUpdate)
                .log().all()
                .when()
                .put("/webhook/employee/update");
// Menampilkan response
        System.out.println("Response: " + response.asPrettyString());
    }

    @Test(dependsOnMethods = "testLogin", priority = 3)
    public void testDeleteEmployee(){
        // Base URL
        RestAssured.baseURI = "https://whitesmokehouse.com";
         // Mengirim permintaan DELETE ke endpoint delete employee
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .delete("/webhook/employee/delete");
        // Print the response
        System.out.println("Response: " + response.asPrettyString());
    }

    @Test
    public void testGetAllEmployee(){
        // Base URL
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
        // Base URL
        RestAssured.baseURI = "https://whitesmokehouse.com";
        
        // Mengirim request GET employee dengan token yang tidak valid
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
}
