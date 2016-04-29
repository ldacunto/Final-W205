from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
import psycopg2
import os

SQLALCHEMY_DATABASE_URI = "postgresql+psycopg2://w205:MIDS@localhost/postgres"
e = create_engine(SQLALCHEMY_DATABASE_URI)

app = Flask(__name__)
api = Api(app)

class Count(Resource):
  def get(self, date):
      conn = e.connect()
      print date
      if date.upper() == "GROUP":
         query = conn.execute("select date(time_first),count(*),type from users group by date(time_first),type;")
         result = {"boats": [{"date":i[0], "count":i[1], "type":i[2]} for i in query.cursor.fetchall()]}
      elif date.upper() == "ALL":
         query = conn.execute("select count(*),type from users group by type;")
         result = {"boats": [{"count":i[0], "type":i[1]} for i in query.cursor.fetchall()]}
      else:
         query = conn.execute("select count(*),type from users  where date(time_first) ='%s' group by type;" %date)
         result = {"boats": [{"count":i[0], "type":i[1]} for i in query.cursor.fetchall()]}
      print result
      return result

class Query(Resource):
  def get(self, date):
      conn = e.connect()
      print date
      if date.upper() == "ALL":
         query = conn.execute("select sum(calls),type from users group by type;")
         result = {"boats": [{"queries":i[0], "type":i[1]} for i in query.cursor.fetchall()]}
      else:
         query = conn.execute("select sum(calls),type from users  where date(time_first) ='%s' group by type;" %date)
         result = {"boats": [{"queries":i[0], "type":i[1]} for i in query.cursor.fetchall()]}
      print result
      return result

class List(Resource):
  def get(self, date):
      conn = e.connect()
      print date
      if date.upper() == "ALL":
         query = conn.execute("select userid from users where type != 'GOOD';")
         result = {"boats": [{"id":i[0]} for i in query.cursor.fetchall()]}
      else:
         query = conn.execute("select userid from users where type != 'GOOD' and date(time_first) ='%s';" %date)
         result = {"boats": [{"id":i[0]} for i in query.cursor.fetchall()]}
      print result
      return result
  
class Data(Resource):
  def get(self, id):
      conn = e.connect()
      print id
      if id.upper() == "ALL":
         query = conn.execute("select userid,ipaddress,lon,lat,calls,type,mostwatchedcontent,totaltime,mostwatchedmateches from users;")
         result = {"boats": [{"queries":i[0], "ipaddress":i[1], "lon":i[2], "lat":i[3], "calls":i[4], "type":i[5], "watched":i[6], "total":i[7], "matches":i[8]} for i in query.cursor.fetchall()]}
      else:
         query = conn.execute("select userid,ipaddress,lon,lat,calls,type,mostwatchedcontent,totaltime,mostwatchedmateches from users where userid ='%s';" %id)
         result = {"boats": [{"queries":i[0], "ipaddress":i[1], "lon":i[2], "lat":i[3], "calls":i[4], "type":i[5], "watched":i[6], "total":i[7], "matches":i[8]} for i in query.cursor.fetchall()]}
      print result
      return result
api.add_resource(Count, "/boats/count/<string:date>")
api.add_resource(Query, "/boats/queries/<string:date>")
api.add_resource(List, "/boats/list/<string:date>")
api.add_resource(Data, "/boats/data/<string:id>")

if __name__ == '__main__':
    test_con = e.connect()
    test_query = "SELECT * FROM users LIMIT 1;"
    test_result = test_con.execute(test_query)
    print test_result.cursor.fetchall()
    port = int(os.environ.get("PORT", 8080))
    app.run(host='0.0.0.0', port=port, debug=True)
