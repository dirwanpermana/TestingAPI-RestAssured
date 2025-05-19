package restassured;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.testng.Assert.*;

public class TugasRestAssuredFix {

    private String token; // Global token

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "https://whitesmokehouse.com/webhook";

        // Login valid dan simpan token
        String loginPayload = """
            {
                "email": "dirwantea@gmail.com",
                "password": "Bintaro1!"
            }
            """;

        Response loginResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .log().all()
                .post("/api/login");

        loginResponse.then().log().all();
        assertEquals(loginResponse.getStatusCode(), 200, "Login failed, status code not 200");

        token = loginResponse.jsonPath().getString("token");
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
    }

    @Test
    public void testRegisterAlreadyExist() {
        String registerPayload = """
            {
                "email": "dirwandeui@gmail.com",
                "full_name": "dirwan tea",
                "password": "Bintaro1!",
                "department": "Technology",
                "phone_number": "085590932219"
            }
            """;

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerPayload)
                .log().all()
                .post("/api/register");

        response.then().log().all();

        assertEquals(response.getStatusCode(), 200, "Expected status code 200");
        assertEquals(response.jsonPath().getString("result"), "failed", "Expected result 'failed'");
        assertTrue(response.jsonPath().getString("message").contains("already registered"), "Expected 'already registered' message");
    }

    @Test
    public void getAllObjects() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/api/objects");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        List<Integer> ids = response.jsonPath().getList("id");
        assertTrue(ids.contains(101), "Response should contain ID 101");
    }

    @Test
    public void validateObjectByID() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/api/objectslistId?id=101");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        int id = response.jsonPath().getInt("[0].id");
        String name = response.jsonPath().getString("[0].name");
        String year = response.jsonPath().getString("[0].data.year");
        String price = response.jsonPath().getString("[0].data.price");
        String cpu = response.jsonPath().getString("[0].data.cpu_model");
        String disk = response.jsonPath().getString("[0].data.hard_disk_size");
        String color = response.jsonPath().getString("[0].data.color");
        int capacity = response.jsonPath().getInt("[0].data.capacity");
        int screenSize = response.jsonPath().getInt("[0].data.screen_size");

        assertEquals(id, 101);
        assertEquals(name, "Laptop Gaming Cuy");
        assertEquals(year, "2025");
        assertEquals(price, "23500000");
        assertEquals(cpu, "Intel Core i9");
        assertEquals(disk, "1 TB");
        assertEquals(color, "Black");
        assertEquals(capacity, 2);
        assertEquals(screenSize, 16);
    }

    @Test
    public void validateSingleObject() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/101");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 200);

        assertEquals(response.jsonPath().getInt("id"), 101);
        assertEquals(response.jsonPath().getString("name"), "Laptop Gaming Cuy");
        assertEquals(response.jsonPath().getString("data.year"), "2025");
        assertEquals(response.jsonPath().getString("data.price"), "23500000");
        assertEquals(response.jsonPath().getString("data.cpu_model"), "Intel Core i9");
        assertEquals(response.jsonPath().getString("data.hard_disk_size"), "1 TB");
        assertEquals(response.jsonPath().getString("data.color"), "Black");
        assertEquals(response.jsonPath().getInt("data.capacity"), 2);
        assertEquals(response.jsonPath().getInt("data.screen_size"), 16);
    }

    @Test
    public void createObjectSuccessfully() {
        String createPayload = """
            {
              "name": "Laptop Uhuy",
              "data": {
                "year": "2025",
                "price": 2549.99,
                "cpu_model": "Intel Core i9",
                "hard_disk_size": "1 TB",
                "capacity": "2 cpu",
                "screen_size": "16 Inch",
                "color": "Black"
              }
            }
            """;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(createPayload)
                .post("/api/objects");

        response.then().log().all();

        String name = response.jsonPath().getString("[0].name");
        assertEquals(name, "Laptop Uhuy");
        assertEquals(response.jsonPath().getString("[0].data.year"), "2025");
        assertEquals(response.jsonPath().getDouble("[0].data.price"), 2549.99, 0.01);
        assertEquals(response.jsonPath().getString("[0].data.cpu_model"), "Intel Core i9");
        assertEquals(response.jsonPath().getString("[0].data.hard_disk_size"), "1 TB");
        assertEquals(response.jsonPath().getString("[0].data.capacity"), "2 cpu");
        assertEquals(response.jsonPath().getString("[0].data.screen_size"), "16 Inch");
        assertEquals(response.jsonPath().getString("[0].data.color"), "Black");

        int id = response.jsonPath().getInt("[0].id");
        assertTrue(id > 0, "ID harus lebih dari 0");
    }

    @Test
    public void updateObject() {
        String updatePayload = """
            {
              "name": "Laptop Uhuyan",
              "data": {
                "year": "2025",
                "price": 2549.99,
                "cpu_model": "IntelCorei9",
                "hard_disk_size": "1 TB",
                "capacity": "2 cpu",
                "screen_size": "16 Inch",
                "color": "Black"
              }
            }
            """;

        int objectId = 175;

        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(updatePayload)
                .put("/37777abe-a5ef-4570-a383-c99b5f5f7906/api/objects/" + objectId);

        response.then().log().all();

        assertEquals(response.getStatusCode(), 200);

        assertEquals(response.jsonPath().getInt("[0].id"), objectId);
        assertEquals(response.jsonPath().getString("[0].name"), "Laptop Uhuyan");
        assertEquals(response.jsonPath().getString("[0].data.year"), "2025");
        assertEquals(response.jsonPath().getDouble("[0].data.price"), 2549.99, 0.01);
        assertEquals(response.jsonPath().getString("[0].data.color"), "Black");
        assertEquals(response.jsonPath().getString("[0].data.capacity"), "2 cpu");
        assertEquals(response.jsonPath().getString("[0].data.screen_size"), "16 Inch");
    }

  @Test
public void deleteObjectSuccessfully() {
    RestAssured.baseURI = "https://whitesmokehouse.com/webhook";
    
    // ID object yang akan dihapus
    int objectId = 189;

    // Lakukan request DELETE
    Response response = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .delete("/d79a30ed-1066-48b6-83f5-556120afc46f/api/objects/" + objectId);

    // Log response untuk debugging
    response.then().log().all();

    // Validasi status code
    assertEquals(response.getStatusCode(), 200, "Status code harus 200, tetapi ditemukan " + response.getStatusCode());

    // Validasi response body
    assertEquals(response.jsonPath().getString("status"), "deleted");
    assertEquals(response.jsonPath().getString("message"), "Object with id = " + objectId + ", has been deleted.");
}

    @Test
    public void testGetAllDepartments() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/api/department");

        response.then().log().all();
        assertEquals(response.getStatusCode(), 200);

        List<Map<String, Object>> departments = response.jsonPath().getList("$");

        assertFalse(departments.isEmpty(), "Department list should not be empty");

        boolean hasTechnology = false;
        boolean hasHR = false;
        boolean hasFinance = false;
        boolean hasExecutive = false;

        for (Map<String, Object> dept : departments) {
            int id = (Integer) dept.get("id");
            String department = (String) dept.get("department");

            assertTrue(id > 0, "Department ID should be positive");
            assertNotNull(department, "Department name should not be null");
            assertFalse(department.isEmpty(), "Department name should not be empty");

            if (department.equals("Technology")) hasTechnology = true;
            if (department.equals("Human Resource")) hasHR = true;
            if (department.equals("Finance")) hasFinance = true;
            if (department.equals("Executive")) hasExecutive = true;
        }

        assertTrue(hasTechnology, "Technology department should exist");
        assertTrue(hasHR, "Human Resource department should exist");
        assertTrue(hasFinance, "Finance department should exist");
        assertTrue(hasExecutive, "Executive department should exist");
    }

    @Test
public void verifyPartiallyUpdateObject() {
    // Siapkan payload untuk PATCH (misalnya update sebagian field)
    Map<String, Object> partialUpdate = new HashMap<>();
    partialUpdate.put("name", "Apple MacBook Pro 1611-albert12"); // contoh perubahan
    Map<String, Object> dataUpdate = new HashMap<>();
    dataUpdate.put("year", "2030");
    dataUpdate.put("price", "2549.99");
    partialUpdate.put("data", dataUpdate);

    // Kirim PATCH request
    Response response = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType(ContentType.JSON)
            .body(partialUpdate)
            .patch("/39a0f904-b0f2-4428-80a3-391cea5d7d04/api/object/192");

    // Log response
    response.then().log().all();

    // Validasi status code
    assertEquals(response.getStatusCode(), 200, "Status code is not 200");

    // Ekstrak data
    int id = response.jsonPath().getInt("id");
    String name = response.jsonPath().getString("name");

    // Nested fields
    String year = response.jsonPath().getString("data.year");
    String price = response.jsonPath().getString("data.price");
    String cpu = response.jsonPath().getString("data.cpu_model");
    String disk = response.jsonPath().getString("data.hard_disk_size");
    String color = response.jsonPath().getString("data.color");

    Object capacityRaw = response.jsonPath().get("data.capacity");
    Object screenSizeRaw = response.jsonPath().get("data.screen_size");

    // Validasi tidak null
    assertNotNull(name, "Name should not be null");
    assertNotNull(year, "Year should not be null");
    assertNotNull(price, "Price should not be null");
    assertNotNull(cpu, "CPU model should not be null");
    assertNotNull(disk, "Hard disk size should not be null");
    assertNotNull(color, "Color should not be null");

    // Parsing aman untuk numeric
    String capacityStr = capacityRaw.toString().replaceAll("[^0-9]", "");
    int capacity = capacityStr.isEmpty() ? 0 : Integer.parseInt(capacityStr);

    String screenSizeStr = screenSizeRaw.toString().replaceAll("[^0-9]", "");
    int screenSize = screenSizeStr.isEmpty() ? 0 : Integer.parseInt(screenSizeStr);




    // Validasi isi sesuai ekspektasi
    assertEquals(id, 192, "Incorrect ID");
    assertEquals(name, "Apple MacBook Pro 1611-albert12", "Incorrect name");
    assertEquals(year, "2030", "Incorrect year");
    assertEquals(price, "2549.99", "Incorrect price");
    assertEquals(cpu, "Intel Core i9", "Incorrect CPU");
    assertEquals(disk, "1 TB", "Incorrect disk size");
    assertEquals(color, "Black", "Incorrect color");
   // Validasi nilai
    assertEquals(capacity, 2, "Incorrect capacity");
    assertEquals(screenSize, 16, "Incorrect screen size");
}
}

