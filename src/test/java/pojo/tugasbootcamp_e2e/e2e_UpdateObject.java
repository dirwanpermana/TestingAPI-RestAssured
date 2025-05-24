package pojo.tugasbootcamp_e2e;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seleniumproject.model.modelTugas_e2e.listObjectDetail;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Melakukan testing untuk update data object baru dan verify perubahan object
public class e2e_UpdateObject extends FungsiLogin{
    private String tokenLogin;
    private final int objectId = 175;
    private listObjectDetail oldObject;
    private Map<String, Object> dataBaru;

    // Melakukan login dan menyimpan token
    @BeforeClass
    public void setup() throws Exception {
        tokenLogin = getTokenLogin("dirwan100@gmail.com", "Bintaro1!");
    }

    // Verify data object sebelum di update
    @Test(priority = 1)
    public void getObjectLama() throws Exception {
        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + tokenLogin)
                .queryParam("id", objectId)
                .when()
                .get("/api/objectslistId");

        Assert.assertEquals(response.statusCode(), 200);

        ObjectMapper mapper = new ObjectMapper();
        List<listObjectDetail> objects = mapper.readValue(response.asString(), new TypeReference<List<listObjectDetail>>() {});
        Assert.assertFalse(objects.isEmpty(), "Object tidak ditemukan!");
        oldObject = objects.get(0);

        System.out.println("=== OBJECT DATA SEBELUM DI UPDATE ===");
        System.out.println("Name: " + oldObject.name);
        System.out.println("CPU: " + oldObject.data.cpu_model);
        System.out.println("Capacity: " + oldObject.data.capacity);
    }

    // Lakukan update data object
    @Test(priority = 2, dependsOnMethods = "getObjectLama")
    public void updateObjectById() {
        dataBaru = new HashMap<>();
        // Update data disini dan bagian assertion
        dataBaru.put("year", "2025");
        dataBaru.put("price", 2549.99);
        dataBaru.put("cpu_model", "IntelCorei9");
        dataBaru.put("hard_disk_size", "2 TB");
        dataBaru.put("color", "Black");
        dataBaru.put("capacity", "2 cpu");
        dataBaru.put("screen_size", "16");

        Map<String, Object> body = new HashMap<>();
        body.put("name", "Laptop Dirwan");
        body.put("data", dataBaru);

        Response responseUpdate = RestAssured
                .given()
                .header("Authorization", "Bearer " + tokenLogin)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/37777abe-a5ef-4570-a383-c99b5f5f7906/api/objects/" + objectId);

        Assert.assertEquals(responseUpdate.statusCode(), 200);
        System.out.println("Update berhasil.");
    }

    // Verify data object setelah di update
    @Test(priority = 3, dependsOnMethods = "updateObjectById")
    public void VerifyObjectSetelahUpdate() throws Exception {
        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + tokenLogin)
                .queryParam("id", objectId)
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

        Assert.assertEquals(obj.name, "Laptop Dirwan");
        Assert.assertEquals(obj.data.cpu_model, dataBaru.get("cpu_model"));
        Assert.assertEquals(obj.data.capacity, 2); // karena "2 cpu"
        Assert.assertEquals(obj.data.year, dataBaru.get("year"));
        Assert.assertEquals(obj.data.color, dataBaru.get("color"));
        Assert.assertEquals(obj.data.hard_disk_size, dataBaru.get("hard_disk_size"));

        System.out.println("=== OBJECT DETAILS AFTER UPDATE ===");
        System.out.println("Name: " + obj.name);
        System.out.println("CPU: " + obj.data.cpu_model);
        System.out.println("Capacity: " + obj.data.capacity);
        System.out.println("Year: " + obj.data.year);
        System.out.println("Screen Size: " + obj.data.screen_size);
        System.out.println("Color: " + obj.data.color);
        System.out.println("Hard Disk: " + obj.data.hard_disk_size);
        System.out.println("Price: " + obj.data.price);
    }
}

