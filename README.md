RESTful Dropbox like Service - StoreBox 
======================

$ mvn clean package

# How to run this Java process forever
$ nohup ./bin/dev.sh 0<&- &> /tmp/app.log &


Things We Learnt:
Practical Applications of:
1) RestFul Services
2) MongoDB
3) Jquery 
4) Ajax
5) Amazon Web Services
6) Working with bootstrap and mustach

Features of our project:
1) File upload from system to StoreBox
2) File download to local machine
3) Delete files
4) Share files --> Sharing would add the file in shared users bucket too
5) Per user storage capacity by default 15MB(for the purpose of testing), this can be extended. 

AWS services used:
1) EC2 to deploy
2) S3 to store files
3) SES to send emails for registration confirmation & when a file is shared.

While running the code please update the following in dropboxService.java(main class)
	MongoConfig.setAmazonUsername("XXXXXXXXXXXXXX");               --> AWS Access key
	MongoConfig.setAmazonPassword("XXXXXXXXXXXXXXXXXXXXXXXXx");    --> AWS Secret key
	MongoConfig.setDatabaseAddress("XXXXXXXXXXXXXX");              --> Mongo lab address of the form dsXXXXXX.mongolab.com
	MongoConfig.setDatabasePassword("XXXXXXXXXXXXX");              --> Mongo lab password
	MongoConfig.setDatabasePort(XXXXX);                            --> Port # obtained from Mongo lab
	MongoConfig.setDatabaseUsername("XXXXXXXXXXXX");               --> MongoDB username


Service runs on Port # 8050
XX.XX.XX.XX:8050/dropbox




