@regression
# validation tests by removing one field at a time and creating a task and to see if error messages are appropriate
Feature: test correct error message for invalid task creation

  Background:
    * url baseUrl
    * def Const = call read('classpath:Karate/Common.feature')
    * def getRand = Const.getRand
    * def getStartDate = Const.getStartDate
    * def getEndDate = Const.getEndDate

  Scenario: No name given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.name = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask name NotNull}," }

  Scenario: Name given as empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.name = ""
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400

  Scenario: Description not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.description = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask description NotNull}," }

  Scenario: Description given as an empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.description = ""
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400

  Scenario: Group not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.group = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask group NotNull}," }

  Scenario: Group given as an empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.group = ""
    * def datetime1 = getStartDate(1)
    * def datetime2 = getEndDate(60)
    * set task.trigger.startDateTime = datetime1
    * set task.trigger.endDateTime = datetime2

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message : "{scheduleTask group NotBlank}," }

  Scenario: Creator not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.creator = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask creator NotNull}," }

  Scenario: Creator given as an empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.creator = ""
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400

  Scenario: Trigger not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger = null

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger NotNull}," }

  Scenario: Start date not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger.startDateTime = null
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger.startDateTime NotNull}," }

  Scenario: Start date given as an empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger.startDateTime = ""
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger.startDateTime NotNull}," }

  Scenario: End date not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger.endDateTime = null
    * set task.trigger.startDateTime = getStartDate(1)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger.endDateTime NotNull}," }

  Scenario: End date given as an empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger.endDateTime = ""
    * set task.trigger.startDateTime = getStartDate(1)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger.endDateTime NotNull}," }

  Scenario: End date before start date
    * def task = read('classpath:tasks/simpleTask.json')
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * def datetime1 = getStartDate(1)
    * def datetime2 = getEndDate(60)
    * set task.trigger.endDateTime = datetime1
    * set task.trigger.startDateTime = datetime2

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400

  Scenario: Trigger schedule not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger.schedule = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger.schedule NotNull},{scheduleTask trigger.schedule NotNull}," }

  Scenario: Trigger schedule value not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger.schedule.value = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger.schedule.value NotNull},{scheduleTask trigger.schedule.value NotNull}," }

  Scenario: Trigger schedule value given as empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * set task.trigger.schedule.value = ""
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)


    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "schedule.value cronexpression is invalid" }

  Scenario: Trigger schedule type not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger.schedule.type = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

      #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger.schedule.type NotNull},{scheduleTask trigger.schedule.type NotNull}," }

  Scenario: Trigger schedule type given as empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.trigger.schedule.type = ""
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

      #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask trigger.schedule.type NotNull},{scheduleTask trigger.schedule.type NotNull}," }

  Scenario: Predefined expression not defined
    * def task = read('classpath:tasks/simpleTask.json')
    * def randnum = getRand()
    * def datetime1 = getStartDate(1)
    * def datetime2 = getEndDate(60)
    * set task.name = task.name + randnum
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)
    * set task.trigger.schedule.type = "predefinedExpression"
    * set task.trigger.schedule.value = "everyday"

      #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400

  Scenario: Cron expression of less than min allowed interval
    * def task = read('classpath:tasks/simpleTask.json')
    * def randnum = getRand()
    * set task.name = task.name + randnum
    * set task.trigger.schedule.value = "0 0/1 * * * ?"
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "Cron expression interval cannot be less than min allowed interval" }

  Scenario: Executor not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.executor = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask executor NotNull}," }

  Scenario: Executor type not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.executor.type = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask executor.type NotNull},{scheduleTask executor.type NotNull}," }

  Scenario: Executor type given as empty string
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.executor.type = ""
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask executor.type NotNull},{scheduleTask executor.type NotNull}," }

  Scenario: Curl config not given for type curl
    * def task = read('classpath:tasks/simpleTask.json')
    * def randnum = getRand()
    * def datetime1 = getStartDate(1)
    * def datetime2 = getEndDate(60)
    * set task.name = task.name + randnum
    * set task.trigger.startDateTime = datetime1
    * set task.trigger.endDateTime = datetime2
    * set task.executor.curlConfig = null

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400

  Scenario: Curl command not given
    * def task = read('classpath:tasks/simpleTask.json')
    * set task.executor.curlConfig.curlCommand = null
    * set task.trigger.startDateTime = getStartDate(1)
    * set task.trigger.endDateTime = getEndDate(60)

    #test for correct error message
    Given path 'schedule-task/'
    And request task
    When method post
    Then status 400
    And match $ contains any { error: "Bad Request", message: "{scheduleTask executor.curlConfig.curlCommand NotNull}," }