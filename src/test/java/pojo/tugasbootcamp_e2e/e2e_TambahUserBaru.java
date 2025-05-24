package pojo.tugasbootcamp_e2e;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumproject.model.modelTugas_e2e.listObjectDetail;
import com.seleniumproject.model.modelTugas_e2e.reqRegist;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class e2e_TambahUserBaru extends FungsiLogin {

    private String baseURI = "https://whitesmokehouse.com/webhook";
    private String tokenLogin;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = baseURI;
    }

    // Melakukan Register kemudian login
    @Test
    public void testRegisterAndLogin() throws Exception {
        // ObjectMapper mapper = new ObjectMapper();

        // REGISTER 
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

        // LOGIN via FungsiLogin
        tokenLogin = getTokenLogin(email, password);
        Assert.assertNotNull(tokenLogin, "Token dari login tidak ditemukan!");
        System.out.println("TOKEN LOGIN: " + tokenLogin);
    }

    // Melakukan vencarian data berdasarkan id
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

        ObjectMapper mapper = new ObjectMapper();
        List<listObjectDetail> objects = mapper.readValue(response.asString(),
                new TypeReference<List<listObjectDetail>>() {});

        Assert.assertFalse(objects.isEmpty(), "Response list kosong!");
        listObjectDetail obj = objects.get(0);

        Assert.assertEquals(obj.id, idToSearch);
        Assert.assertEquals(obj.name, "Laptop dirwan");
        Assert.assertEquals(obj.data.cpu_model, "IntelCorei9");
        Assert.assertEquals(obj.data.capacity, 2);

        System.out.println("=== OBJECT DETAILS ===");
        System.out.println("ID: " + obj.id);
        System.out.println("Name: " + obj.name);
        System.out.println("CPU: " + obj.data.cpu_model);
        System.out.println("Year: " + obj.data.year);
        System.out.println("Screen Size: " + obj.data.screen_size);
    }
}
