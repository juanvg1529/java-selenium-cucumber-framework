@ui
Feature: Purchase
  As a logged-in customer
  I want to buy products
  So that they are shipped to me

  Background:
    Given the customer is logged in

  @smoke
  Scenario Outline: Buy a single product - <product>
    When the customer adds these products to the cart:
      | <product> |
    And the customer checks out as "Juan" "Vera" with postal code "050001"
    Then the order overview lists exactly the products in the cart
    And the order is paid with "SauceCard" and shipped by "Pony Express"
    And the order total is the item total plus tax
    When the customer finishes the order
    Then the order is confirmed with "Thank you for your order!"
    And the cart is empty

    Examples:
      | product                           |
      | Sauce Labs Bike Light             |
      | Test.allTheThings() T-Shirt (Red) |
      | Sauce Labs Onesie                 |

  @regression
  Scenario: Buy several products after removing one from the cart
    When the customer adds these products to the cart:
      | Sauce Labs Bolt T-Shirt  |
      | Sauce Labs Fleece Jacket |
      | Sauce Labs Backpack      |
    And the customer removes "Sauce Labs Fleece Jacket" from the cart
    Then the cart badge shows 2
    When the customer checks out as "Juan" "Vera" with postal code "050001"
    Then the order overview lists exactly the products in the cart
    And the order total is the item total plus tax
    When the customer finishes the order
    Then the order is confirmed with "Thank you for your order!"

  @regression
  Scenario: Buy the second product after sorting by name from Z to A
    When the customer sorts the products by "Name (Z to A)"
    And the customer adds the product in position 2 to the cart
    And the customer checks out as "Juan" "Vera" with postal code "050001"
    Then the order overview lists exactly the products in the cart
    When the customer finishes the order
    Then the order is confirmed with "Thank you for your order!"

  @negative
  Scenario: Checkout requires the customer's details
    When the customer adds these products to the cart:
      | Sauce Labs Backpack |
    And the customer checks out as "" "Vera" with postal code "050001"
    Then the checkout error "Error: First Name is required" is shown
