Feature: What is available for private clients?
  Check options for private clients

  Scenario: "Private clients" links is moving into submenu
    Given opened page is ""
    When I click link with text "Private clients"
    Then I should see "Accounts and cards" link
    Then I should see "Mortgages" link

  Scenario Outline: "Banking packages for private clients" has got correct packages links
    Given opened page is "Private clients.Accounts and cards.Banking packages"
    When I click link with text "<banking_package_link>"
    Then I should open page "<banking_package_URL>" with "<correct_title>"
  Examples:
    | banking_package_link           | banking_package_URL                                                          | correct_title                                                |
    | Banking Package UBS Generation | https://www.ubs.com/ch/en/private/accounts-and-cards/bundles/generation.html | Generation: Free account for young people \| UBS Switzerland |
    | Banking Package UBS Campus     | https://www.ubs.com/ch/en/private/accounts-and-cards/bundles/campus.html     | Campus: Free account for students \| UBS Switzerland         |

  Scenario: Currency converter calculates correctly 10x bigger conversion
    Given opened page is "Private clients.Accounts and cards.Banking packages"
    When I click link with text "Currency converter"
    Then I should see page with "Currency converter: current exchange rates | UBS Switzerland" title
    When I click element with JS and text "To currency converter"
    Then I should see amount field
    When I enter "1000000" in the amount field and it is calculated
    Then I can get conversion value
    When I enter "10000000" in the amount field and it is calculated
    Then I can get conversion value and it is 10x bigger
