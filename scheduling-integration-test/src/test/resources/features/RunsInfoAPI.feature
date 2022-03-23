@sanity @ft
# Call runs API to get execution times
Feature: Test runs api

  Background:
    * url baseUrl
    * def Const = call read('classpath:Karate/Common.feature')
    * def getRand = Const.getRand
    * def getCurrentTime = Const.getCurrentTime
    * def getStartDate = Const.getStartDate
    * def getEndDate = Const.getEndDate
    * def getCount = Const.getCount

  Scenario: Verify that is equal to 4
    * def trigger = read('classpath:tasks/trigger.json')
    * def datetime1 = getStartDate(3)
    * def datetime2 = getEndDate(63)
    * set trigger.startDateTime = datetime1
    * set trigger.endDateTime = datetime2
    * set trigger.schedule.value = "0 0/15 * * * ?"

    Given path 'schedule-task/runs-info'
    And request trigger
    When method post
    Then status 200
    * assert getCount(response) == 4

  Scenario: Verify that exception is thrown when frequency is less than minimum allowed
    * def trigger = read('classpath:tasks/trigger.json')
    * def datetime1 = getStartDate(3)
    * def datetime2 = getEndDate(63)
    * set trigger.startDateTime = datetime1
    * set trigger.endDateTime = datetime2
    * set trigger.schedule.value = "0 0/2 * * * ?"

    #create task
    Given path 'schedule-task/runs-info'
    And request trigger
    When method post
    Then status 400
    * assert response.code == "40011"
    * match response.message contains "less than min allowed interval"
