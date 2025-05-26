Feature: Employee API

  Background:
    Given The base url in this feature is "https://whitesmokehouse.com/webhook"

  Scenario: Register a new user
    When Send a http "POST" request to "/api/register" with body:
      """
      {
        "email": "dirwan543@gmail.com",
        "full_name": "dirwan tea",
        "password": "Bintaro1!",
        "department": "Technology",
        "phone_number": "085590932219"
      }
      """
    Then The response status must be 200
    And The response schema should be match with schema "schema_register.json"

  Scenario: Login user and save token
    When Send a http "POST" request to "/api/login" with body:
      """
      {
        "email": "dirwan543@gmail.com",
        "password": "Bintaro1!"
      }
      """
    Then The response status must be 200
    And Save the token from the response to local storage
