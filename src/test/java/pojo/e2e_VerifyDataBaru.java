package pojo;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumproject.model.modelTugas_e2e.listObjectDetail;
import com.seleniumproject.model.modelTugas_e2e.reqLogin;
import com.seleniumproject.model.modelTugas_e2e.reqRegist;
import com.seleniumproject.model.modelTugas_e2e.resLogin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class e2e_VerifyDataBaru {
private String baseURI = "https://whitesmokehouse.com/webhook";
    private String tokenLogin;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseURI;
    }

    // Test Penambahan user baru dan melakukan verify object
    @Test
    public void testRegisterAndLogin() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // ==== REGISTER ====
        String email = "dirwan" + System.currentTimeMillis() + "@gmail.com";
        String full_name = "dirwan tea";
        String password = "Bintaro1!";
        String department = "Technology";
        String phone_number = "085590932219";

        reqRegist registerData = new reqRegist(email, full_name, password, department, phone_number);

        Response responseRegister = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerData)
                .log().all()
                .when()
                .post("/api/register");

        Assert.assertEquals(responseRegister.statusCode(), 200);
        System.out.println("REGISTER RESPONSE:\n" + responseRegister.asPrettyString());

        // ==== LOGIN ====
        reqLogin loginData = new reqLogin(email, "Bintaro1!");

        Response responseLogin = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginData)
                .log().all()
                .when()
                .post("/api/login");

        Assert.assertEquals(responseLogin.statusCode(), 200);
        System.out.println("LOGIN RESPONSE:\n" + responseLogin.asPrettyString());

        // Deserialization response login
        resLogin loginResponse = mapper.readValue(responseLogin.asString(), resLogin.class);

        // Verifikasi token tidak null
        Assert.assertNotNull(loginResponse.token, "Token dari login tidak ditemukan!");
        tokenLogin = loginResponse.token;
        System.out.println("TOKEN LOGIN: " + tokenLogin);
    }

    // Verify List Object by ID 
    @Test(priority = 2, dependsOnMethods = {"testRegisterAndLogin"})
    public void getObjectById() throws Exception {
        int idToSearch = 175;

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + tokenLogin)
                .queryParam("id", idToSearch)
                .log().all()
                .when()
                .get("/api/objectslistId");

        Assert.assertEquals(response.statusCode(), 200);
        System.out.println("RESPONSE:\n" + response.asPrettyString());

        // Deserialize response (karena response-nya array)
        ObjectMapper mapper = new ObjectMapper();
        List<listObjectDetail> objects = mapper.readValue(response.asString(),
                new TypeReference<List<listObjectDetail>>() {});

        // Verifikasi isi object pertama
        Assert.assertFalse(objects.isEmpty(), "Response list kosong!");
        listObjectDetail obj = objects.get(0);

        Assert.assertEquals(obj.id, idToSearch);
        Assert.assertEquals(obj.name, "Laptop Uhuyan");
        Assert.assertEquals(obj.data.cpu_model, "IntelCorei9");
        Assert.assertEquals(obj.data.capacity, 2);

        // Tampilkan list data object
        System.out.println("=== OBJECT DETAILS ===");
        System.out.println("ID: " + obj.id);
        System.out.println("Name: " + obj.name);
        System.out.println("CPU: " + obj.data.cpu_model);
        System.out.println("Year: " + obj.data.year);
        System.out.println("Screen Size: " + obj.data.screen_size);
    }


    
}