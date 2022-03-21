@sanity
# create a task, execute it once and then delete. Also test creation of schedules with
# different pre defined cron expressions
Feature: create, delete, execute now and get features with a valid task

  Background:
    * url baseUrl
    * def Const = call read('classpath:Karate/Common.feature')
    * def getRand = Const.getRand
    * def getStartDate = Const.getStartDate
    * def getEndDate = Const.getEndDate
    * def getCount = Const.getCount
    * def sleepForSec = Const.sleepForSec


  Scenario: creating and deleting a task
    * configure retry = { count: 10, interval: 6000 }
    * def task = read('classpath:tasks/task.json')
    * def datetime1 = getStartDate(3)
    * def datetime2 = getEndDate(10)
    * set task.trigger.startDateTime = datetime1
    * set task.trigger.endDateTime = datetime2
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * set task.executor.httpConfig.url = mockUrl+'callback/200'

    #testing creation
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 201
    And match $ contains any {status: "ACTIVE"}

    * string id = response.id
    * print '1 ValidTaskCreateAndDelete -- created task ' + response.name + ' with task id ' + response.id

    #execute task
    Given path 'schedule-task/'+id+'/execute-now/'
    And request ''
    When method post
    Then status 200

    * call sleepForSec 60

    #get task
    Given path 'schedule-task/'+id
    When method get
    Then status 200
    And match response.id == id

    #testing creation conflict
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 409
    And match $ contains any {error: "conflict", message: "Schedule Task with same name already exist for this group"}

    * call sleepForSec 2

    #testing deletion
    Given path 'schedule-task/'+id
    When method delete
    Then status 204
    * print '2 ValidTaskCreateAndDelete -- deleted task with id ' + id

    #get task when no task
    Given path 'schedule-task/'+id
    When method get
    Then status 404
    And match $ contains any { error: "Not Found", message: "Schedule Task id does not exist" }

    #delete task when no task
    Given path 'schedule-task/'+id
    When method delete
    Then status 404
    And match $ contains any { error: "Not Found", message: "Schedule Task id does not exist" }

    #check that execution is reflected in mock service response
    Given url mockUrl+'schedule/'+id
    And retry until getCount(response) > 0
    When method get
    Then status 200

    * print '3 ValidTaskCreateAndDelete -- task ' + id + ' mock response ' + response
    * assert getCount(response) == 1

  Scenario Outline: Creating tasks with predefined cron expressions
    * def task = read('classpath:tasks/simpleTask.json')
    * def getEndDateLong =
    """
    function() {
    var Const = Java.type('com.mediaiq.caps.platform.scheduling.integration.utils.TimeUtils');
    var value = Const.getCurrentDatePlusMonths(2);
    return value;
    }
    """
    * def datetime1 = getStartDate(5)
    * def datetime2 = getEndDateLong()
    * set task.trigger.startDateTime = datetime1
    * set task.trigger.endDateTime = datetime2
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * set task.trigger.schedule.type = "predefinedExpression"
    * set task.trigger.schedule.value = "<expression>"

    #create task
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 201
    And match $ contains any { status: "ACTIVE", task.trigger.schedule.value: "<expression>" }


    * def id = response.id
    * print '4 ValidTaskCreateAndDelete -- task with predefined cron expression ' + response.trigger.schedule.value + ' and id ' + response.id + ' created'

    #delete task
    Given path 'schedule-task/'+id
    When method delete
    Then status 204

    Examples:
      | expression |
      | every3h    |
