package restassured;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class TugasRestAssured {
    String token;   // Variabel global

    @Test
    public void testRegisterAllreadyExist(){
        RestAssured.baseURI = "https://whitesmokehouse.com/webhook";

        String requestBody = "{\n" +
                "  \"email\": \"dirwandeui@gmail.com\",\n" +
                "  \"full_name\": \"dirwan tea\",\n" +  
                "  \"password\": \"Bintaro1!\",\n" +         
                "  \"department\": \"Technology\",\n" +     
                "  \"phone_number\": \"085590932219\"\n" +
                "}";

        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .post("/api/register");

        System.out.println("Response: " + response.asPrettyString());

        assert response.getStatusCode() == 200 : "Expected 200, got " + response.getStatusCode();
        String result = response.jsonPath().getString("result");
        String message = response.jsonPath().getString("message");

        assert result.equals("failed") : "Expected result 'failed' but got '" + result + "'";
        assert message.contains("already registered") : "Expected message to contain 'already registered' but got '" + message + "'";
    }

    @BeforeTest
    public void LoginValid() {
        RestAssured.baseURI = "https://whitesmokehouse.com/webhook"; 

        String requestBody = "{\n" +
                "  \"email\": \"dirwantea@gmail.com\",\n" +
                "  \"password\": \"Bintaro1!\"\n" +
                "}";

        Response response = RestAssured.given()
                .contentType("application/json")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .log().all()
                .post("/api/login");

        System.out.println("Response: " + response.asPrettyString());

        assert response.getStatusCode() == 200 : "Expected 200, got " + response.getStatusCode();

        // Simpan token ke variabel global
        token = response.jsonPath().getString("token");

        assert token != null && !token.isEmpty() : "Token not returned or empty";
    }

    @Test
    public void getAllObjects() {
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/api/objects");

        response.then().log().all();

        // Validasi respon code dan check id 101
        assert response.statusCode() == 200 : "Expected 200, got " + response.statusCode();

        List<Integer> idList = response.jsonPath().getList("id");
        assertTrue(idList.contains(101), "Expected ID 101 not found in response");
    }

    @Test
    public void validateObjectByID() { 
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/api/objectslistId?id=101");

        // Log response
        response.then().log().all();

        // Assert status code
        assertEquals(response.getStatusCode(), 200, "Status code is not 200");

        // Extract data
        int id = response.jsonPath().getInt("[0].id");
        String name = response.jsonPath().getString("[0].name");
        String year = response.jsonPath().getString("[0].data.year");
        String price = response.jsonPath().getString("[0].data.price");
        String cpu = response.jsonPath().getString("[0].data.cpu_model");
        String disk = response.jsonPath().getString("[0].data.hard_disk_size");
        String color = response.jsonPath().getString("[0].data.color");
        int capacity = response.jsonPath().getInt("[0].data.capacity");
        int screenSize = response.jsonPath().getInt("[0].data.screen_size");

        // Assert values
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
    public void ValidateSingleObject() { 
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/8749129e-f5f7-4ae6-9b03-93be7252443c/api/objects/101");

        // Log response
        response.then().log().all();

        // Validate status code
        assertEquals(response.getStatusCode(), 200, "Unexpected status code");

        // Validate JSON fields
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
        RestAssured.baseURI = "https://whitesmokehouse.com/webhook";

          String requestBody = "{\n" +
            "  \"name\": \"Laptop Uhuy\",\n" +
            "  \"data\": {\n" +
            "    \"year\": \"2025\",\n" +
            "    \"price\": 2549.99,\n" +
            "    \"cpu_model\": \"Intel Core i9\",\n" +
            "    \"hard_disk_size\": \"1 TB\",\n" +
            "    \"capacity\": \"2 cpu\",\n" +
            "    \"screen_size\": \"16 Inch\",\n" +
            "    \"color\": \"Black\"\n" +
            "  }\n" +
            "}";

    Response response = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .body(requestBody)
            .post("/api/objects");

    response.then().log().all();

    // Ambil data dari response list (jika response berupa array)
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
    public void UpdateObject() {
        RestAssured.baseURI = "https://whitesmokehouse.com/webhook";

      String requestBody = "{\n" +
            "  \"name\": \"Laptop Uhuyan\",\n" +
            "  \"data\": {\n" +
            "    \"year\": \"2025\",\n" +
            "    \"price\": 2549.99,\n" +
            "    \"cpu_model\": \"IntelCorei9\",\n" +  // Perhatikan spasi di "CPU model"
            "    \"hard_disk_size\": \"1 TB\",\n" +      // Perhatikan spasi di "Hard disk size"
            "    \"capacity\": \"2 cpu\",\n" +
            "    \"screen_size\": \"16 Inch\",\n" +
            "    \"color\": \"Black\"\n" +
            "  }\n" +
            "}";

    // ID object yang akan diupdate
    int objectId = 175;

    // Lakukan request PUT untuk update object
    Response response = RestAssured.given()
            .header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .body(requestBody)
            .put("/37777abe-a5ef-4570-a383-c99b5f5f7906/api/objects/" + objectId);

    // Log response untuk debugging
    response.then().log().all();

    // Validasi status code
    assertEquals(response.getStatusCode(), 200, "Status code harus 200, tetapi ditemukan " + response.getStatusCode());

    // Validasi response body (response berupa array dengan 1 elemen)
    assertEquals(response.jsonPath().getInt("[0].id"), objectId);
    assertEquals(response.jsonPath().getString("[0].name"), "Laptop Uhuyan");
    
    // Validasi data object - gunakan format field yang sama dengan response
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
    int objectId = 187;

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
        // Send GET request
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .get("/api/department");

        // Log response for debugging
        response.then().log().all();

        // Validate status code
        assertEquals(response.getStatusCode(), 200, "Expected status code 200");

        // Parse response as list of maps
        List<Map<String, Object>> departments = response.jsonPath().getList("$");

        // Validate the list is not empty
        assertTrue(departments.size() > 0, "Department list should not be empty");

        // Validate specific department entries
        boolean hasTechnology = false;
        boolean hasHR = false;
        boolean hasFinance = false;
        boolean hasExecutive = false;

        for (Map<String, Object> dept : departments) {
            int id = (Integer) dept.get("id");
            String department = (String) dept.get("department");

            // Validate each department has required fields
            assertTrue(id > 0, "Department ID should be positive");
            assertTrue(department != null && !department.isEmpty(), "Department name should not be empty");

            // Check for specific departments
            if (department.equals("Technology")) hasTechnology = true;
            if (department.equals("Human Resource")) hasHR = true;
            if (department.equals("Finance")) hasFinance = true;
            if (department.equals("Executive")) hasExecutive = true;
        }

        // Validate expected departments exist
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
    // Parsing capacity dan screen size dengan aman
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