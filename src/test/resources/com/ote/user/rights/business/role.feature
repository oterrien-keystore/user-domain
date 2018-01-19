Feature: User Rights

  Background: Define default privilege hierarchy
    Given the following privileges:
      | code  | parent |
      | Admin |        |
      | Write | Admin  |
      | Read  | Write  |

  Scenario: User not found
    Given the user 'steve.jobs' owns following rights on application 'Apple':
      | perimeter | privilege |
      | GLE       | Write     |
    When I want to check user 'john.doe' and application 'Apple' owns privilege 'Write' on perimeter 'GLE'
    Then an exception of type 'UserNotFoundException' should be thrown

  Scenario: Application not found
    Given the user 'steve.jobs' owns following rights on application 'Apple':
      | perimeter | privilege |
      | GLE       | Write     |
    When I want to check user 'steve.jobs' and application 'Pixar' owns privilege 'Write' on perimeter 'GLE'
    Then an exception of type 'ApplicationNotFoundException' should be thrown

  Scenario: Perimeter not found
    Given the user 'steve.jobs' owns following rights on application 'Apple':
      | perimeter | privilege |
      | GLE       | Write     |
    When I want to check user 'steve.jobs' and application 'Apple' owns privilege 'Write' on perimeter 'ACCOUNT'
    Then an exception of type 'PerimeterNotFoundException' should be thrown

  Scenario: Privilege not found
    Given the user 'steve.jobs' owns following rights on application 'Apple':
      | perimeter | privilege |
      | GLE       | Write     |
    When I want to check user 'steve.jobs' and application 'Apple' owns privilege 'OtherPrivilege' on perimeter 'GLE'
    Then an exception of type 'PrivilegeNotFoundException' should be thrown

  Scenario: Right not found
    Given the user 'steve.jobs' owns following rights on application 'Apple':
      | perimeter | privilege |
      | GLE       | Write     |
      | Account   | Read      |
    When I want to check user 'steve.jobs' and application 'Apple' owns privilege 'Write' on perimeter 'Account'
    Then I should get 'False'

  Scenario: Right found
    Given the user 'steve.jobs' owns following rights on application 'Apple':
      | perimeter | privilege |
      | GLE       | Write     |
      | Account   | Read      |
    When I want to check user 'steve.jobs' and application 'Apple' owns privilege 'Read' on perimeter 'Account'
    Then I should get 'True'

  Scenario: Right found in upper level
    Given the user 'steve.jobs' owns following rights on application 'Apple':
      | perimeter | privilege |
      | DEAL      | Read      |
      | DEAL/GLE  | Write     |
    When I want to check user 'steve.jobs' and application 'Apple' owns privilege 'Read' on perimeter 'DEAL/GLE'
    Then I should get 'True'

  Scenario: Right found in upper privilege
    Given the user 'steve.jobs' owns following rights on application 'Apple':
      | perimeter | privilege |
      | Account   | Write     |
    When I want to check user 'steve.jobs' and application 'Apple' owns privilege 'Read' on perimeter 'Account'
    Then I should get 'True'