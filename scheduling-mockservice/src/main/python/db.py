import sqlite3

from flask import g, current_app

DATABASE = "sqlite/scheduling_mockservice.db"


def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect(DATABASE)

        def make_dicts(cursor, row):
            return dict((cursor.description[idx][0], value)
                        for idx, value in enumerate(row))

        db.row_factory = make_dicts
    return db


def init_db():
    db = get_db()
    with current_app.open_resource('schema.sql') as f:
        db.executescript(f.read().decode('utf8'))


def query_db(query, args=(), one=False):
    cur = get_db().execute(query, args)
    rv = cur.fetchall()
    cur.close()
    return (rv[0] if rv else None) if one else rv


def sql_edit_insert(query, var):
    cur = get_db().cursor()
    cur.execute(query, var)
    get_db().commit()


def sql_delete(query, var):
    cur = get_db().cursor()
    cur.execute(query, var)
