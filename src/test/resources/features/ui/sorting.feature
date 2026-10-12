@ui @regression
Feature: Product sorting
  Customers can sort the catalogue to find products faster.

  Background:
    Given the customer is logged in

  Scenario Outline: Sort products by <option>
    When the customer sorts the products by "<option>"
    Then the products are ordered by <field> <direction>

    Examples:
      | option              | field | direction  |
      | Name (A to Z)       | name  | ascending  |
      | Name (Z to A)       | name  | descending |
      | Price (low to high) | price | ascending  |
      | Price (high to low) | price | descending |
