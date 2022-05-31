@sanity @ft
# create a schedule with cron expression of an absolute time and check that it executes at correct time
Feature: test execution for different time zones

  Background:
    * url baseUrl
    * def Const = call read('classpath:Karate/Common.feature')
    * def getRand = Const.getRand
    * def getCurrentTime = Const.getCurrentTime
    * def getStartDate = Const.getStartDateInTimezone
    * def getEndDate = Const.getEndDateInTimezone
    * def getCount = Const.getCount
    * def getDateTimeMillis = Const.getDateTimeMillis
    * def isValid = Const.isValid
    * def shift = Const.shift
    * def getAbsoluteCron =
    """
    function(date) {
      var Const = Java.type('com.miqdigital.scheduling.integration.utils.TimeUtils');
      var value = Const.getAbsoluteCron(date);
      return value;
    }
    """

  Scenario: test regular execution of schedule
    * configure retry = { count: 20, interval: 60000 }
    # task with absolute cron expression
    * def task1 = read('classpath:tasks/task.json')
    * def datetime1 = getStartDate("America/New_York")
    * def datetime2 = getEndDate(9,"America/New_York")
    * set task1.trigger.startDateTime = datetime1
    * set task1.trigger.endDateTime = datetime2
    * set task1.trigger.schedule.value = getAbsoluteCron(datetime1)
    * def randnum = getRand()
    * set task1.name = task1.name + randnum
    * set task1.executor.httpConfig.url = mockUrl+'callback/200'

    # tasks with different schedule frequencies
    * def task2 = read('classpath:tasks/task.json')
    * def datetime1 = getStartDate("Australia/Sydney")
    * def datetime2 = getEndDate(9,"Australia/Sydney")
    * set task2.trigger.startDateTime = datetime1
    * set task2.trigger.endDateTime = datetime2
    * set task2.trigger.schedule.value = "0 0/5 * * * ?"
    * def randnum = getRand()
    * set task2.name = task2.name + randnum
    * set task2.executor.httpConfig.url = mockUrl+'callback/200'

    * def task3 = read('classpath:tasks/task.json')
    * def datetime1 = getStartDate("Asia/Tokyo")
    * def datetime2 = getEndDate(4,"Asia/Tokyo")
    * def endTime = getEndDate(15,"UTC")
    * print '1 timeZone -- endTime ' + endTime
    * set task3.trigger.startDateTime = datetime1
    * set task3.trigger.endDateTime = datetime2
    * set task3.trigger.schedule.type = "everyNMinutes"
    * set task3.trigger.schedule.value = "5"
    * def randnum = getRand()
    * set task3.name = task3.name + randnum
    * set task3.executor.httpConfig.url = mockUrl+'callback/200'

    #testing creation of task1
    Given path 'schedule-task/'
    And request task1
    When method post
    Then status 201
    And match $ contains any { status: "ACTIVE" }

    * string id1 = response.id
    * print '2 timeZone -- created task1 ' + response.name + ' with task1 id ' + response.id

    #testing creation of task2
    Given path 'schedule-task/'
    And request task2
    When method post
    Then status 201
    And match $ contains any { status: "ACTIVE" }

    * string id2 = response.id
    * print '3 timeZone -- created task2 ' + response.name + ' with task2 id ' + response.id

    #testing creation of task3
    Given path 'schedule-task/'
    And request task3
    When method post
    Then status 201
    And match $ contains any { status: "ACTIVE" }

    * string id3 = response.id
    * print '4 timeZone -- created task3 ' + response.name + ' with task3 id ' + response.id

    #get run info of task1
    Given path 'schedule-task/runs-info'
    And request task1.trigger
    When method post
    Then status 200

    * def runsInfoResponseTask1 = response
    * print '5 timeZone -- runsInfoResponseTask1 -- ' + runsInfoResponseTask1
    * def runsInfoCountTask1 = getCount(runsInfoResponseTask1)

    #get run info of task2
    Given path 'schedule-task/runs-info'
    And request task2.trigger
    When method post
    Then status 200

    * def runsInfoResponseTask2 = response
    * print '6 timeZone -- runsInfoResponseTask2 -- ' + runsInfoResponseTask2
    * def runsInfoCountTask2 = getCount(runsInfoResponseTask2)

    #get run info of task3
    Given path 'schedule-task/runs-info'
    And request task3.trigger
    When method post
    Then status 200

    * def runsInfoResponseTask3 = response
    * print '7 timeZone -- runsInfoResponseTask3 -- ' + runsInfoResponseTask3
    * def runsInfoCountTask3 = getCount(runsInfoResponseTask3)

    * retry until getDateTimeMillis(getCurrentTime()) > getDateTimeMillis(endTime) + 180*1000
    * print '7-- timeZone -- endTime ' + endTime

    #check if task1 executes at correct times
    Given url mockUrl+'schedule/'+id1
    When method get
    Then status 200

    * def mockResponseTask1 = $
    * print '8 timeZone -- mockResponseTask1 -- ' + mockResponseTask1
    * def countOfMockCallsTask1 = getCount(mockResponseTask1)

    * assert countOfMockCallsTask1 == runsInfoCountTask1
    # compare to header scheduled time
    * assert isValid(runsInfoResponseTask1, mockResponseTask1) == true

    #check if task2 executes at correct intervals
    Given url mockUrl+'schedule/'+id2
    When method get
    Then status 200

    * def mockResponseTask2 = $
    * print '9 timeZone -- mockResponseTask2 -- ' + mockResponseTask2
    * def countOfMockCallsTask2 = getCount(mockResponseTask2)

    * assert countOfMockCallsTask2 == runsInfoCountTask2
    # compare to header scheduled time
    * assert isValid(runsInfoResponseTask2, mockResponseTask2) == true

    #check if task3 executes at correct intervals
    Given url mockUrl+'schedule/'+id3
    When method get
    Then status 200

    * def mockResponseTask3 = $
    * print '10 timeZone -- mockResponseTask3 -- ' + mockResponseTask3
    * def countOfMockCallsTask3 = getCount(mockResponseTask3)

    * assert countOfMockCallsTask3 == runsInfoCountTask3
    # compare to header scheduled time
    * assert isValid(runsInfoResponseTask3, mockResponseTask3) == true

    #delete task1
    Given url baseUrl+'schedule-task/'+id1
    When method delete
    Then status 204

    #delete task2
    Given url baseUrl+'schedule-task/'+id2
    When method delete
    Then status 204

    #delete task3
    Given url baseUrl+'schedule-task/'+id3
    When method delete
    Then status 204

