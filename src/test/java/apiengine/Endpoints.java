package apiengine;

import helper.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Endpoints {
public Endpoints() {
        // Set the base URI for the API
        String baseUrl = ConfigManager.getBaseUrl();
        RestAssured.baseURI = baseUrl;
    }
// Mengambil base URL dari konfigurasi menggunakan ConfigManager, lalu mengatur baseURI RestAssured
    public Response login(String bodyRequest) {
        Response responseLogin = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(bodyRequest)
                .log().all()
                .when()
                .post("/api/login");
        return responseLogin;
    }

    // GET All Object
    public Response getAllObject(String token) {
        Response responseGetObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .log().all()
                .when()
                .get("/api/objects");
        return responseGetObject;
    }

    // Update Object
    public Response updateObject(String bodyRequest, String token) {
        // Integer id = 175;
        Response responseUpdateObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(bodyRequest)
                .log().all()
                .when()
                .put("/37777abe-a5ef-4570-a383-c99b5f5f7906/api/objects/175");
        return responseUpdateObject ;
    }

     public Response addObject(String token, String bodyRequest){
        Response responseAddObject = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(bodyRequest)
                .log().all()
                .when()
                .post("/api/objects");
        return responseAddObject;
    }
}
