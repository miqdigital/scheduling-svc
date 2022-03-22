function() {
  var env = karate.env; // get system property 'karate.env'
  var token = java.lang.System.getenv('TYK_TOKEN');

  var getHeaders = function() {
      var headers = {};

      headers['Content-type'] = 'application/json';
      headers['ENVIRONMENT'] = 'QA';
      headers['DEPARTMENT'] = 'TECH';
      headers['TEAM'] = 'PLATFORMSERVICES';
      headers['SERVICE'] = 'MESSAGING_SERVER';
      headers['PRODUCT'] = 'MESSAGING';
      headers['FUNCTION'] = 'Scheduling Service Integration Tests';
      headers['api-gateway-token'] = "eyJvcmciOiI1ZDYzZDVjYzg0Y2E2NTAwMDE0YWRlMTkiLCJpZCI6IjlmN2Q1YTFkM2ZjYTRiOGU4YzI3NDgxODQ5Mjc4YmQ5IiwiaCI6Im11cm11cjEyOCJ9";
      headers['OWNER'] = 'platform@miqdigital.com';

      return headers;
    };

  if (!env) {
    env = 'local';
  }
  var config = {
    env: env,
    baseUrl: 'http://localhost:8080/',
    mockUrl: java.lang.System.getProperty('MOCK_URL'),
    tagstorun: karate.properties['tagstorun']
  }

  switch(env){
      case 'integration':
          config.baseUrl = 'https://api-gateway.dev.miqdigital.com/integration-scheduling-service/v1/';
          config.mockUrl = 'https://api-gateway.dev.miqdigital.com/integration-scheduling_service_mock/mock/';
          break;
  }

  karate.configure('headers', getHeaders);
  return config;
}
