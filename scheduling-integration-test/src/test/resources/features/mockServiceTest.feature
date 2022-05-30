@sanity @ft
# create a schedule and using runs info determine the correct number of executions and that
# the schedule times match
Feature: test created schedule executes at correct times

  Background:
    * url baseUrl
    * def Const = call read('classpath:Karate/Common.feature')
    * def getRand = Const.getRand
    * def getCurrentTime = Const.getCurrentTime
    * def getStartDate = Const.getStartDate
    * def getEndDate = Const.getEndDate
    * def getCount = Const.getCount
    * def getDateTimeMillis = Const.getDateTimeMillis
    * def isValid = Const.isValid
    * def validExecutionHistory =
    """
    function(arr,str){
      for(i=0;i<arr.length;i++){
        if(arr[0].executionStatus != str)
          return false;
      }
      return true;
    }
    """
    * def getStartDatePast =
    """
    function(min){
      var Const = Java.type('com.miqdigital.scheduling.integration.utils.TimeUtils');
      var value = Const.getCurrentTimePlusMinutes(min);
      return value;
    }
    """

  Scenario: test regular execution of schedule
    * configure retry = { count: 15, interval: 60000 }
    * def task = read('classpath:tasks/task.json')
    * def startTime = getStartDatePast(-60)
    * def datetime1 = getStartDate(3)
    * def datetime2 = getEndDate(9)
    * set task.trigger.startDateTime = datetime1
    * set task.trigger.endDateTime = datetime2
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * set task.executor.httpConfig.url = mockUrl+'callback/200'

    * def task2 = read('classpath:tasks/taskWithError.json')
    * def startTime = getStartDatePast(-60)
    * def datetime1 = getStartDate(3)
    * def datetime2 = getEndDate(9)
    * set task2.trigger.startDateTime = datetime1
    * set task2.trigger.endDateTime = datetime2
    * def randnum = getRand()
    * set task2.name = task2.name + randnum
    * set task2.executor.httpConfig.url = mockUrl+'callback/400'

    #create first schedule
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 201
    And match $ contains any { status: "ACTIVE" }

    * string id = response.id
    * print '1 mockserviceTest -- created task ' + response.name + ' with task id ' + response.id

    #create second schedule
    Given path 'schedule-task/'
    And request task2
    When method post
    Then status 201
    And match $ contains any { status: "ACTIVE" }

    * string id2 = response.id
    * print '2 mockserviceTest -- created task ' + response.name + ' with task id ' + response.id

    #wait for execution to complete
    * retry until getDateTimeMillis(getCurrentTime()) > getDateTimeMillis(datetime2) + 180*1000

    #get run info for first schedule
    Given path 'schedule-task/runs-info'
    And request task.trigger
    When method post
    Then status 200

    * def runsInfoResponse = response
    * print '3 mockserviceTest -- runsInfoResponse' + runsInfoResponse
    * def countOfRuns = getCount(runsInfoResponse)

    #get run info for second schedule
    Given path 'schedule-task/runs-info'
    And request task2.trigger
    When method post
    Then status 200

    * def runsInfoResponse2 = response
    * print '4 mockserviceTest -- runsInfoResponse2' + runsInfoResponse2
    * def countOfRuns2 = getCount(runsInfoResponse2)
    * def endTime = getStartDatePast(60)

    #get runs history for first schedule
    Given path 'schedule-task/runs/group'
    And params { scheduleTaskId: '#(id)', startDateTime: '#(startTime)', endDateTime: '#(endTime)' }
    When method get
    Then status 200

    * def runHistoryResponse = response
    * print '5 mockserviceTest -- runHistoryResponse' + runHistoryResponse
    #check that execution status is correct
    * assert validExecutionHistory(runHistoryResponse, "SUCCESS") == true

    #get runs history for second schedule
    Given path 'schedule-task/runs/group'
    And params { scheduleTaskId: '#(id2)', startDateTime: '#(startTime)', endDateTime: '#(endTime)' }
    When method get
    Then status 200

    * def runHistoryResponse2 = response
    * print '6 mockserviceTest -- runHistoryResponse2' + runHistoryResponse2
    #check that execution status is correct
    * assert validExecutionHistory(runHistoryResponse2, "FAILURE") == true

    #check if first schedule executes at correct intervals
    Given url mockUrl+'schedule/'+id
    When method get
    Then status 200

    * def mockResponse = response
    * print '7 mockserviceTest -- mockResponse' + mockResponse
    * def countOfMockCalls = getCount(response)

    * assert countOfMockCalls == countOfRuns
    # compare to header scheduled time
    * assert isValid(runsInfoResponse, mockResponse) == true

    #delete schedule
    Given url baseUrl+'schedule-task/'+id
    When method delete
    Then status 204

    Given url baseUrl+'schedule-task/'+id2
    When method delete
    Then status 204
