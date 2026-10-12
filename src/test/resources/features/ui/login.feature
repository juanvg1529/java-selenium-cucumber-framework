@ui
Feature: Login
  As a Swag Labs customer
  I want to log in and out of the store
  So that only I can shop with my account

  @smoke
  Scenario: A standard user logs in and logs out
    Given the customer is on the login page
    When the customer logs in with valid credentials
    Then the products page is displayed
    When the customer logs out
    Then the login page is displayed

  @negative
  Scenario Outline: Login is rejected - <case>
    Given the customer is on the login page
    When the customer logs in as "<username>" with password "<password>"
    Then the login error "<error>" is shown

    Examples:
      | case             | username        | password     | error                                                                     |
      | locked out user  | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out.                       |
      | wrong password   | standard_user   | not-it       | Epic sadface: Username and password do not match any user in this service |
      | missing username |                 | secret_sauce | Epic sadface: Username is required                                        |
      | missing password | standard_user   |              | Epic sadface: Password is required                                        |
