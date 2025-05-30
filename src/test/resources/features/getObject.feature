Feature: Login API

  Scenario: Login with valid user
    When Send user to login with body:
      """
      {
        "email": "dirwan100@gmail.com",
        "password": "Bintaro1!"
      }
      """
    Then The response status must be 200
    And Save the token from the response to local storage

# get data all object
    Scenario: Get all objects
        Given Make sure token in local storage not empty
        When Send a request to get all object
        Then The response status must be 200
# Update object
    # Scenario: Update object
    #     Given Make sure token not empty
    #     When Send update object with body:
    #     """
    #     {
    #         "name": "Laptop dirwan",
    #         "data": {
    #                 "year": "2025",
    #                 "price": 2549.99,
    #                 "cpu_model": "IntelCorei9",
    #                 "hard_disk_size": "2 TB",
    #                 "color": "Black",
    #                 "capacity": "2 cpu",
    #                 "screen_size": "16 Inch"
    #             }
    #     }
    #     """
    #     Then The response status must be 200
    #     And name in the response must be "Laptop dirwan"

    Scenario: Create new object
        Given Make sure token in local storage not empty
        When Send create object with body:
        """
        {
            "name": "Laptop si unyil",
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
        And Save the object ID from the response
        And name in the response must be "Laptop si unyil"
