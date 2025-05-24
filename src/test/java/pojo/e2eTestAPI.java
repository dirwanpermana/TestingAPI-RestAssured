package pojo;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumproject.model.RequestLogin;
import com.seleniumproject.model.RequestRegister;
import com.seleniumproject.model.ResponseProvinsi;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class e2eTestAPI {

    private String baseURI = "https://lapor.folkatech.com";
    private String tokenLogin;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseURI;
    }

    @Test   //Testing untuk register dan login
    public void TambahUserBaru() throws JsonProcessingException {
        String name = "dirwan";
        String email = "dirwan100@gmail.com";
        String password = "Qwerty123!";
        String password_confirmation = "Qwerty123!";
        String phone = "0893559090";
        Integer reg_id = 12345;

        // REGISTER
        RequestRegister requestRegister = new RequestRegister(
            name,
            email,
            password,
            password_confirmation,
            phone
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRegister = objectMapper.writeValueAsString(requestRegister);

        Response responseRegister = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonRegister)
                .log().all()
                .when()
                .post("/api/register");

        Assert.assertEquals(responseRegister.getStatusCode(), 200, 
            "Expected status code 200 but got " + responseRegister.getStatusCode());

        System.out.println("REGISTER RESPONSE:\n" + responseRegister.asPrettyString());

        // LOGIN
        RequestLogin requestLogin = new RequestLogin(email, password, reg_id);

        String jsonLogin = objectMapper.writeValueAsString(requestLogin);

        Response responseLogin = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(jsonLogin)
                .log().all()
                .when()
                .post("/api/login");

        Assert.assertEquals(responseLogin.getStatusCode(), 200, 
            "Expected status code 200 but got " + responseLogin.getStatusCode());

        tokenLogin = responseLogin.jsonPath().getString("data.token"); 

        System.out.println("LOGIN RESPONSE:\n" + responseLogin.asPrettyString());
        System.out.println("TOKEN: " + tokenLogin);

       // Verifikasi field response login
        Assert.assertEquals(responseLogin.jsonPath().getInt("code"), 200, "Kode tidak sesuai");
        Assert.assertEquals(responseLogin.jsonPath().getString("message"), "User logged in", "Pesan tidak sesuai");
        Assert.assertTrue(responseLogin.jsonPath().getBoolean("success"), "false");
        Assert.assertEquals(responseLogin.jsonPath().getString("data.email"), email, "Email tidak sesuai");
        Assert.assertEquals(responseLogin.jsonPath().getString("data.name"), name, "Nama tidak sesuai");
        Assert.assertNotNull(responseLogin.jsonPath().getString("data.token"), "Token login null!");
        Assert.assertEquals(responseLogin.jsonPath().getString("data.phone_number"), phone, "Nomor HP tidak sesuai");

        // Simpan token untuk test lanjutan
        tokenLogin = responseLogin.jsonPath().getString("data.token");
        System.out.println("TOKEN: " + tokenLogin);
    }

    // Testing Get data provinsi
@Test(priority = 2, dependsOnMethods = {"TambahUserBaru"})
    public void VerifyListProvinsi() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Response resProvinsi = RestAssured
                .given()
                .header("Authorization", "Bearer " + tokenLogin)
                .when()
                .get(baseURI + "/api/province");

        // Verifikasi status code
        Assert.assertEquals(resProvinsi.statusCode(), 200, "Status code bukan 200");

        // Deserialize response ke POJO
        ResponseProvinsi VerifyListProvinsi = objectMapper.readValue(resProvinsi.asString(), ResponseProvinsi.class);

        // Verifikasi isi response
        Assert.assertTrue(VerifyListProvinsi.status, "Field 'status' harus true");
        Assert.assertEquals(VerifyListProvinsi.message, "Success", "Pesan response tidak sesuai");
        Assert.assertNotNull(VerifyListProvinsi.data, "Data provinsi null");
        Assert.assertFalse(VerifyListProvinsi.data.isEmpty(), "Data provinsi kosong");

        // Cari provinsi tertentu
        boolean foundJawaBarat = VerifyListProvinsi.data.stream()
                .anyMatch(p -> p.name.equalsIgnoreCase("Jawa Barat"));
        Assert.assertTrue(foundJawaBarat, "'Jawa Barat' tidak ditemukan dalam daftar provinsi");

        // Tampilkan daftar provinsi
        System.out.println("Daftar Provinsi:");
        for (ResponseProvinsi.Province prov : VerifyListProvinsi.data) {
            System.out.println("- ID: " + prov.id + ", Name: " + prov.name);
        }
    }
}