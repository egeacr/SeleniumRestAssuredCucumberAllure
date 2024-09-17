@brand @regression @api
Feature: Brand Endpoint Tests

  @createBrand @smoke
  Scenario: Brand Create Test
    Given User Create a new brand
    Then user verifies that request is completed successfuly with 201

  @getBrand @smoke
  Scenario: Brand Search Test
    Given User search created brand with id
    Then user verifies that request is completed successfuly with 200

  @updateBrand @smoke
  Scenario: Brand Update Test
    And user update created brand
    Then user verifies that request is completed successfuly with 200
