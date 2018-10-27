# Simple Employee project
Small project demonstrating the use of REST services with spring boot and mongodb, including error handling, authentication, authorization, documentation and of course unit testing

The compiled executable program runs a tomcat server located by default in http://localhost:8080/employee


## DB External Initialization
By default, the program loads a internal js script using [Mongeez change management tool](https://github.com/mongeez/mongeez) to initialize the database with 10 employee records. If you wish to load an external set of data, please follow the next instructions:
1. Create a js file which will have mongodb inserts.
  1. The js file can have any name, but it must have `.js` extension
1. Start the file with the next comments. Replace `[username]` with your name:
```javascript
//mongeez formatted javascript
//changeset [username]:ChangeSet-1
```
1. Then add all your inserts with the next format:
```javascript
db.employee.insert({
    "_id" : "1", "firstName" : "Leonardo", "middleNameInitial":"T", "lastName": "Da Vinci", "birthdate": new Date("1984-05-01"), "employmentDate": new Date("1987-12-28"), "status": true, "created": new Date(), "lastUpdated": new Date, "_class": "com.jondelatorre.employee.model.Employee"
});
```
1. Save the file
1. Create an xml file with the next contents. Replace `[init-db-file-name.js]` with the name of the file you previously created:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<changeFiles>
    <file path="[init-db-file-name.js]"/>
</changeFiles>
```
1. Put any name to xml file and save it in the same directory where your `js` file is located.
1. Run the program adding the next argument: ``. Replace `[/path/load-db.xml]` with the full path or relative path and the name of the `xml` file you previously created. You can also put the `xml` and `js` files in the same folder than your `jar`; e.g.:
```bash
$ java -jar employee-0.0.1-SNAPSHOT.jar --mongeez.location=file:///C:/Development/load-init.xml
```
or
```bash
$ java -jar employee-0.0.1-SNAPSHOT.jar --mongeez.location=load-init.xml
```
1. The program should start loading the inserts you defined.

If you want to see an example of the `xml` and `js` files, pleae look at [mongeez.xml](../jondelatorre/master/src/main/resources/mongeez.xml) and [mongeez.js](../jondelatorre/master/src/main/resources/mongeez.js)