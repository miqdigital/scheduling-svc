function() {
  var env = karate.env; // get system property 'karate.env'

  var getHeaders = function() {
      var headers = {};
      headers['Content-type'] = 'application/json';
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

  karate.configure('headers', getHeaders);
  return config;
}
