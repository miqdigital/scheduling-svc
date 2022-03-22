import datetime
import flask
import json
import logging
import pytz as pytz
import time
from flask import request, g, Response

logging.basicConfig(
    format='%(asctime)s,%(msecs)d %(levelname)-8s [%(filename)s:%(lineno)d] %(message)s',
    datefmt='%Y-%m-%d:%H:%M:%S',
    level=logging.DEBUG)

logger = logging.getLogger(__name__)

import db

app = flask.Flask(__name__)
with app.app_context():
    db.init_db()


@app.route('/health')
def healthy():
    db.query_db('SELECT 1')
    return ''


@app.route('/callback/<int:http_status>', methods=['GET'])
def callback_status(http_status):
    curr_time = datetime.datetime.now(pytz.utc).strftime("%Y-%m-%dT%H:%M:%S%z")
    flag = 0
    schedule_id = request.headers.get('schedule-task-id') if request.headers.get(
        'schedule-task-id') is not None else None
    called_dict = {
        'status': http_status,
        'called': curr_time,
        'id': schedule_id
    }
    query_params = request.args if request.args.to_dict() else None
    headers = str(request.headers) if request.headers else None
    path_params = request.view_args if request.view_args else None
    scheduled_time = request.headers.get('schedule-task-scheduled-time') if request.headers.get(
        'schedule-task-scheduled-time') is not None else None
    start_time = request.headers.get('schedule-task-start-time') if request.headers.get(
        'schedule-task-start-time') is not None else None
    logging.info("Schedule Task: %s called with status %s at %s", schedule_id, http_status,
                 scheduled_time)
    try:
        db.sql_edit_insert('''Insert into mockResponse values(?,?,?,?,?,?,?,?)''', (schedule_id,
                                                                                    http_status,
                                                                                    scheduled_time,
                                                                                    start_time,
                                                                                    curr_time,
                                                                                    headers,
                                                                                    json.dumps(
                                                                                        path_params),
                                                                                    json.dumps(
                                                                                        query_params)))
        logging.info("db query succeeded for %s", schedule_id)
    except Exception as ex:
        logging.error("db query failed for %s with exception %s", schedule_id, ex)
        return Response({"message": "Insert into table failed"}, 500)
    return Response(json.dumps(called_dict), http_status)


@app.route('/callback/delay/<int:delay_in_seconds>/status_code/<http_status>', methods=['GET'])
def delay_status(delay_in_seconds, http_status):
    curr_time = datetime.datetime.now(pytz.utc).strftime("%Y-%m-%dT%H:%M:%S%z")
    time.sleep(delay_in_seconds)
    schedule_id = request.headers.get('schedule-task-id') if request.headers.get(
        'schedule-task-id') is not None else None
    called_dict = {
        'status': http_status,
        'called': curr_time,
        'id': schedule_id
    }
    query_params = request.args if request.args.to_dict() else None
    headers = str(request.headers) if request.headers else None
    path_params = request.view_args if request.view_args else None
    scheduled_time = request.headers.get('schedule-task-scheduled-time') if request.headers.get(
        'schedule-task-scheduled-time') is not None else None
    start_time = request.headers.get('schedule-task-start-time') if request.headers.get(
        'schedule-task-start-time') is not None else None
    logging.info("Schedule Task: %s called with status %s and delay %s at %s", schedule_id,
                 http_status, delay_in_seconds, scheduled_time)
    try:
        db.sql_edit_insert('''Insert into mockResponse values(?,?,?,?,?,?,?,?)''', (schedule_id,
                                                                                    http_status,
                                                                                    scheduled_time,
                                                                                    start_time,
                                                                                    curr_time,
                                                                                    headers,
                                                                                    json.dumps(
                                                                                        path_params),
                                                                                    json.dumps(
                                                                                        query_params)))
        logging.info("db query succeeded for %s", schedule_id)
    except Exception as ex:
        logging.error("db query failed for %s with exception %s", schedule_id, ex)
        return Response({"message": "Insert into table failed"}, 500)
    return Response(json.dumps(called_dict), http_status)


@app.route('/schedule/<schedule_id>', methods=['GET'])
def get_schedule(schedule_id):
    try:
        response = db.query_db('Select * from mockResponse where id = ?', [schedule_id], one=False)
        logging.info("db query succeeded for %s , response %s", schedule_id, response)
    except Exception as ex:
        logging.error("db query failed for %s with exception %s", schedule_id, ex)
        return Response({"message": "db query failed"}, 500)
    return Response(json.dumps(response))


@app.teardown_appcontext
def close_connection(exception):
    _db = getattr(g, '_database', None)
    if _db is not None:
        _db.close()


if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0', threaded=True)
