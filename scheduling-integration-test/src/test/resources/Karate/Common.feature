@ignore
Feature: Defining common functions for writing features

  Scenario: Functions common between features
    * def getRand =
    """
    function() {
    var math = Java.type('java.lang.Math');
    var value = math.random();
    return value;
    }
    """
    * def getCurrentTime =
    """
    function() {
    var Const = Java.type('com.miqdigital.scheduling.integration.utils.TimeUtils');
    var value = Const.getCurrentTime();
    return value;
    }
    """
    * def getStartDate =
    """
    function(mins) {
    var Const = Java.type('com.miqdigital.scheduling.integration.utils.TimeUtils');
    var value = Const.getCurrentTimePlusMinutes(mins);
    return value;
    }
    """
    * def getStartDateInTimezone =
    """
    function(tz) {
    var Const = Java.type('com.miqdigital.scheduling.integration.utils.TimeUtils');
    var value = Const.getCurrentTimePlusMinutes(3, tz);
    return value;
    }
    """
    * def getEndDate =
    """
    function(mins) {
    var Const = Java.type('com.miqdigital.scheduling.integration.utils.TimeUtils');
    var value = Const.getCurrentTimePlusMinutes(mins);
    return value;
    }
    """
    * def getEndDateInTimezone =
    """
    function(mins,tz) {
    var Const = Java.type('com.miqdigital.scheduling.integration.utils.TimeUtils');
    var value = Const.getCurrentTimePlusMinutes(mins, tz);
    return value;
    }
    """
    * def getCount =
    """
    function(result) {
    var count = result.length;
    return count;
    }
    """
    * def getDateTimeMillis =
    """
    function(date) {
    var Const = Java.type('com.miqdigital.scheduling.integration.utils.TimeUtils');
    var value = Const.getMillis(date);
    return value;
    }
    """
    * def isValid =
    """
    function(arrRuns, arrMock) {
      for(var i=0; i<arrRuns.length; i++){
        if(((getDateTimeMillis(arrRuns[i])*10)/10) !== ((getDateTimeMillis(arrMock[i].scheduleTime)*10)/10)){
          return false;
        }
      }
      return true;
    }
    """
    * def shift =
    """
    function(arr){
      var res = [];
      for(i=0;i<(--arr.length);){
        res[i] = arr[++i];
      }
      return res;
    }
    """
    * def sleepForSec =
        """
        function(x) {
            karate.log('sleeping for ' + x + ' seconds');
            java.lang.Thread.sleep(x*1000);
        }
        """
