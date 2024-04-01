@test
Feature: Simple API Testing for Jsonplaceholder

  Scenario: User send GET and validate json schema
    Given User send GET to the API
    Then the status code is 200
    And response content type is JSON
    Then validate schema json response "placeholderschema"

  Scenario: User send POST and check response body
    Given User send POST to the API
      """
      {
        "title" : "recommendation",
        "body" : "motorcycle",
        "userId" : 12
      }
      """
    Then the status code is 201
    And response content type is JSON
    Then the body response content should be matched string
      | key     | value          |
      | title   | recommendation |
      | body    | motorcycle     |
    Then the body response content should be matched integer
      | key     | value          |
      | id      | 101             |
      | userId  | 12             |