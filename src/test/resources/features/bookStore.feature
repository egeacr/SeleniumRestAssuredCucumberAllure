@regression @ui @bookSearch
Feature: Book Store Search Box Tests

  @existingBookSearch @smoke
  Scenario: User can search existing books and see the results
    Given User navigates to books store page
    And user searches books with "Git" keyword
    Then user verifies that result are displayed


  @existingBookSearch @smoke
  Scenario: User can search existing books and see the results
    Given User navigates to books store page
    And user searches existing book with full book name
    Then user verifies that result are displayed

  @nonExistingBookSearch @smoke
  Scenario: User can search non-existing books
    Given User navigates to books store page
    And user searches books with "Playwright" keyword
    Then user verifies that there is not any displayed result


