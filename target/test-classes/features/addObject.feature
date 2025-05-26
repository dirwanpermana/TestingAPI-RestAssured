Feature: Add new object to the system

  Background:
    Given The base url in this feature is "https://whitesmokehouse.com/webhook"
    When Send a http "POST" request to "/api/login" with body:
      """
      {
        "email": "dirwan100@gmail.com",
        "password": "Bintaro1!"
      }
      """
    Then The response status must be 200
    And Save the token from the response to local storage

  Scenario: Successfully add a new object via generic POST step
    When Send a http "POST" request to "/api/objects" with body:
      """
      {
        "name": "PC Gaming",
        "data": {
          "year": 2025,
          "price": 2549.99,
          "cpu_model": "Intel Core i9",
          "hard_disk_size": "1 TB",
          "capacity": "2 cpu",
          "screen_size": "16 Inch",
          "color": "Black"
        }
      }
      """
    Then The response status must be 200
    And The response schema should be match with schema "schema_addObject.json"
    And The response body should contain "name" with value "PC Gaming"