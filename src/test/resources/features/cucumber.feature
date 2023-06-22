Feature: First test
  Cucumber will generate an error message if a step definition registers an unknown parameter type, but the suite will run.
  Scenario: Hello world
    Given GET demo request 1
    When POST demo request 1
    When Test SOAP API GET
    Then POST demo request 2
