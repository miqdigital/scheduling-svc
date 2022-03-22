@sanity @ft
# create a schedule, during its fist execution delete and see if first execution completes
Feature: test delete during execution feature

  Background:
    * url baseUrl
    * def Const = call read('classpath:Karate/Common.feature')
    * def getRand = Const.getRand
    * def getCurrentTime = Const.getCurrentTime
    * def getStartDate = Const.getStartDate
    * def getEndDate = Const.getEndDate
    * def getCount = Const.getCount
    * def getDateTimeMillis = Const.getDateTimeMillis
    * def isValid =
    """
    function(arrMock) {
      if(arrMock.length>0)
        return true;
      return false;
    }
    """

  Scenario: create a schedule and delete during execution
    * configure retry = { count: 20, interval: 60000 }
    * def task = read('classpath:tasks/task.json')
    * def datetime1 = getStartDate(3)
    * def datetime2 = getEndDate(18)
    * set task.trigger.startDateTime = datetime1
    * set task.trigger.endDateTime = datetime2
    * set task.trigger.schedule.value = "0 0/5 * * * ?"
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * set task.executor.httpConfig.url = mockUrl+'callback/delay/5/status_code/200'

      #create task
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 201
    And match $ contains any {status: "ACTIVE"}

    * def id = response.id
    * print '1 deleteDuringExecute -- created task ' + response.name + ' with task id ' + response.id

    #get runs info
    Given path 'schedule-task/runs-info'
    And request task.trigger
    When method post
    Then status 200

    * def runsInfoResponse = response
    * print '2 deleteDuringExecute -- runsInfoResponse -- ' + runsInfoResponse
    * def countOfRuns = getCount(runsInfoResponse)

    #get schedule
    Given path 'schedule-task/'+id
    And retry until getDateTimeMillis(getCurrentTime()) > getDateTimeMillis(runsInfoResponse[0]) + 180*1000
    When method get
    Then status 200

    #delete schedule
    Given path 'schedule-task/'+id
    When method delete
    Then status 204

    #get mock response
    Given url mockUrl+'schedule/'+id
    And retry until isValid(response) == true
    When method get
    Then status 200

    * def mockResponse = response
    * print '3 deleteDuringExecute -- mockresponse -- ' + mockResponse

    * print '4 deleteDuringExecute -- runsInfoResponse -- ' + runsInfoResponse

    * match getDateTimeMillis(runsInfoResponse[0]) == getDateTimeMillis(mockResponse[0].scheduleTime)
