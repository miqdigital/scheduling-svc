DROP TABLE IF EXISTS mockResponse;

CREATE TABLE mockResponse (
  id TEXT NOT NULL,
  status INTEGER NOT NULL,
  scheduleTime TEXT NOT NULL,
  startTime TEXT NOT NULL,
  calledTime TEXT NOT NULL,
  headers TEXT,
  pathParams TEXT,
  queryParams TEXT
);