from pymongo import MongoClient


client = MongoClient("127.0.0.1", 27017)

db = client["nmdp_conversion"]

db.create_collection(name="hml")
db.create_collection(name="fhir")
db.create_collection(name="conversionStatus")
db.create_collection(name="fhirSubmission")

client.close()
