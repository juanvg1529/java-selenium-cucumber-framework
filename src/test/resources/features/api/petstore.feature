@api
Feature: Petstore API
  The pet store keeps its catalogue of pets up to date through a REST API.

  @smoke
  Scenario: Manage a pet through its whole lifecycle
    Given a new pet named "Rocky" tagged "puppy"
    When the pet is added to the store
    Then the response status is 200
    And the store returns the pet exactly as sent
    When the pet's name is changed to "Rocky Balboa" and its status to "sold"
    Then the response status is 200
    And the store returns the pet exactly as sent
    When the pet is deleted
    Then the response status is 200
    And the pet can no longer be found

  @regression
  Scenario: Available pets can be listed by status
    When the customer lists pets with status "available"
    Then the response status is 200
    And every pet in the list has status "available"

  @negative
  Scenario: Requesting a pet that does not exist returns 404
    When the customer requests the pet with id 0
    Then the response status is 404
    And the error message is "Pet not found"
