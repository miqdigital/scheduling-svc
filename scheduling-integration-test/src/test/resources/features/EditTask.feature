@sanity @ft
# create a schedule, update it and see if update is successful
Feature: test edit task feature

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
    * def shift = Const.shift
    * def sleepForSec = Const.sleepForSec

  Scenario: create and then edit a task
    * def task = read('classpath:tasks/simpleTask.json')
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * def datetime1 = getStartDate(3)
    * def datetime2 = getEndDate(17)
    * set task.trigger.startDateTime = datetime1
    * set task.trigger.endDateTime = datetime2

    #create task
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 201
    And match $ contains any {status: "ACTIVE"}

    * def id = response.id
    * print '1 EditTask -- created task' + response.name + ' with task id ' + response.id

    #get task
    Given path 'schedule-task/'+id
    When method get
    Then status 200

    * match response.name == task.name
    * match response.description == task.description
    * match response.trigger.schedule.value == task.trigger.schedule.value

    * def update = response

    * set update.name = "SimpleTask_HttpBinEvery6Minutes"+randnum
    * set update.description = "Simple task to call httpbin every 6 minutes"
    * set update.trigger.schedule.value = "0 0/6 * * * ?"

    #edit task name, description and cron expression
    Given path 'schedule-task/'+id
    And request update
    When method put
    Then status 200

    * match response.name == "SimpleTask_HttpBinEvery6Minutes"+randnum
    * match response.description == "Simple task to call httpbin every 6 minutes"
    * match response.trigger.schedule.value == "0 0/6 * * * ?"

    * print '2 EditTask -- edittask1 name, description and cron expression of task with id ' + id + ' have been updated'

    #delete task
    Given path 'schedule-task/'+id
    When method delete
    Then status 204

  #create a schedule, during it's first execution update it, check that first execution
  # completes and subsequent executions follow new schedule
  Scenario: test updation during execution
    * configure retry = { count: 15, interval: 60000 }
    * def task = read('classpath:tasks/taskWithDelay.json')
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * def datetime1 = getStartDate(3)
    * def datetime2 = getEndDate(20)
    * set task.trigger.startDateTime = datetime1
    * set task.trigger.endDateTime = datetime2
    * set task.executor.httpConfig.url = mockUrl+'callback/delay/5/status_code/200'

    * call sleepForSec 10
      #create task
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 201
    And match $ contains any {status: "ACTIVE"}

    * def id = response.id
    * print '3 EditTask -- created task ' + response.name + ' with task id ' + response.id

      #get runs info
    Given path 'schedule-task/runs-info'
    And request task.trigger
    When method post
    Then status 200

    * def runsInfoResponseBeforeUpdate = response
    * print '4 EditTask -- edittask2 runsInfoResponseBeforeUpdate  ' + runsInfoResponseBeforeUpdate
    * def countOfRunsBeforeUpdate = getCount(runsInfoResponseBeforeUpdate)

    * retry until getDateTimeMillis(getCurrentTime()) > getDateTimeMillis(runsInfoResponseBeforeUpdate[0]) + 180*1000

    #get schedule
    Given path 'schedule-task/'+id
    When method get
    Then status 200

    * def update = response

    * set update.name = "MockserviceTask_Updated"+randnum
    * set update.description = "Simple task to call mockservice every 7 minutes with delay"
    * set update.trigger.schedule.value = "0 0/7 * * * ?"
    * set update.trigger.startDateTime = getStartDate(3)
    * set update.trigger.endDateTime = getStartDate(11)
    * print '5 EditTask -- edittask2 update ' + update

      #update schedule frequency
    Given path 'schedule-task/'+id
    And request update
    When method put
    Then status 200

      #get runs info
    Given path 'schedule-task/runs-info'
    And request update.trigger
    When method post
    Then status 200

    * def runsInfoResponseAfterUpdate = response
    * print '6 EditTask -- edittask2 runsInfoResponseAfterUpdate  ' + runsInfoResponseAfterUpdate
    * def countOfRunsAfterUpdate = getCount(runsInfoResponseAfterUpdate)

    * configure retry = { count: 20, interval: 60000 }
    * retry until getDateTimeMillis(getCurrentTime()) > getDateTimeMillis(update.trigger.endDateTime) + 180*1000

    #get mock response
    Given url mockUrl+'schedule/'+id
    When method get
    Then status 200

    * def mockResponse = response
    * print '7 EditTask -- edittask2 mockResponse  ' + mockResponse
    * def countOfMockCalls = getCount(response)

    * print '8 EditTask -- edittask2 end runsInfoResponseBeforeUpdate -- ' + runsInfoResponseBeforeUpdate
    * print '9 EditTask -- edittask2 end runsInfoResponseAfterUpdate -- ' + runsInfoResponseAfterUpdate

    * match isValid(runsInfoResponseAfterUpdate, shift(mockResponse)) == true

    #delete schedule
    Given url baseUrl+'schedule-task/'+id
    When method delete
    Then status 204

