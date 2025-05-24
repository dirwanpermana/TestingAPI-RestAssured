// Class fungsi login ini sebagai parent class agar bisa digunakan di banyak case

package pojo.tugasbootcamp_e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumproject.model.modelTugas_e2e.reqLogin;
import com.seleniumproject.model.modelTugas_e2e.resLogin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;

public class FungsiLogin {

    protected final String baseURI = "https://whitesmokehouse.com/webhook";

    protected String getTokenLogin(String email, String password) throws Exception {
        RestAssured.baseURI = baseURI;

        reqLogin login = new reqLogin(email, password);
        Response response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post("/api/login");

        Assert.assertEquals(response.getStatusCode(), 200, "Login gagal!");

        resLogin loginResp = new ObjectMapper().readValue(response.asString(), resLogin.class);
        Assert.assertNotNull(loginResp.token, "Token login tidak boleh null!");

        return loginResp.token;
    }
}
