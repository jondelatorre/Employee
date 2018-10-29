# Simple Employee project
Small project demonstrating the use of REST services with spring boot and mongodb; including error handling, authentication,  documentation and of course unit testing.

The compiled executable program runs a tomcat server located by default in http://localhost:8080/employee. It connects to an in-memory mongodb database started at a random port. You can see details about mongodb database in the log.

## How to run the project
The project has been created with maven and spring boot. So, you have 2 options to run it:
### Download the executable jar file from release
1. Download the releae file and run it in command line (terminal) window using the next command:
```bash
$ java -jar employee-1.0.0-RELEASE.jar
```
### Build and run the source code
In order to compile and build the source code, you are going to need maven. Once you have installed it, you can build the code by running the next command at `employee` folder, where the `pom.xml` file is located:
```bash
$ mvn package
```
Once the process is completed, you will see a `target` folder inside `employee` folder and there it will be the executable jar file named `employee-1.0.0-RELEASE.jar`. Once you see it, you can follow the same instructions from "Download the executable jar file from release" section

## How to use the program
As I previously mentioned, when you run the executable jar file, it starts a tomcat server at http://localhost:8080/employee . Once it is running, you can use your favourite REST client to make some calls to the api. It supports 5 different calls. All of them requires you to send at least the next header: `Accept:application/json` . POST and PUT also require: `Content-Type:application/json`. DELETE has basic authentication security, so it will need `Authorization` header too.
The program loads 10 employees by default. If you want to see them, just call GET /employee url.

### POST /employee
This method will create employees in the database. It requires the next data:
#### Required data for the request
1. Headers
    1. `Accept:application/json`
    1. `Content-Type:application/json`
1. Body
    1. You can see an example of the body [here](./src/test/resources/input-sample.json)

#### Response
The response body will have the created employee in json format.

### GET /employee
This method will retrieve all ACTIVE employees from the database.
#### Required data for the request
1. Headers
    1. `Accept:application/json`

#### Response
The response body will have an array of ACTIVE employees retrieved from the database.

### GET /employee/{employeeId}
This method will retrieve a single ACTIVE employee from the database.
#### Required data for the request
1. Headers
    1. `Accept:application/json`
1. Path Parameters
    1. `{employeeId}`: Replace it with the actual employee id you want to retrieve, e.g.:1

#### Response  
The response body will have a single employee retrieved from the database in case it exists. If it doesn't exists it will return a 404 error.

### PUT /employee/{employeeId}
This method will update a single ACTIVE employee in the database.
#### Required data for the request
1. Headers
    1. `Accept:application/json`
    1. `Content-Type:application/json`
1. Path Parameters
    1. `{employeeId}`: Replace it with the actual employee id you want to update, e.g.:1
1. Body
    1. You can see an example of the body [here](./src/test/resources/input-sample.json)

#### Response  
The response body will have the update employee retrieved in the database in case it exists. If it doesn't exists it will return a 404 error.

### DELETE /employee/{employeeId}
This method will soft delete a single ACTIVE employee in the database. It requires the user to be authenticated in order to allow this call. Details for authentication are the next:
1. Username: user
1. Password: password
#### Required data for the request
1. Headers
    1. `Authorization: Basic {base64(user:password)}`
        1. Replace {base64(user:password)} with the actual base64 encoding for username and password.
1. Path Parameters
    1. `{employeeId}`: Replace it with the actual employee id you want to delete, e.g.:1
#### Response
The response body will be empty in case the operation succeeds. If the employee does not exist or the employee has been already deleted, it will return an error message in json format.

## DB External Initialization
By default, the program loads a internal js script using [Mongeez change management tool](https://github.com/mongeez/mongeez) to initialize the database with 10 employee records. If you wish to load an external set of data, please follow the next instructions:
1. Create a js file which will have mongodb inserts.
    1. The js file can have any name, but it must have `.js` extension
1. Start the file with the next comments. Replace `[username]` with your name:
```javascript
//mongeez formatted javascript
//changeset [username]:ChangeSet-1
```
3. Then add all your inserts with the next format:
```javascript
db.employee.insert({
    "_id" : NumberLong("1"), "firstName" : "Leonardo", "middleNameInitial":"T", "lastName": "Da Vinci", "birthdate": new Date("1984-05-01"), "employmentDate": new Date("1987-12-28"), "status": true, "created": new Date(), "lastUpdated": new Date, "_class": "com.jondelatorre.employee.model.Employee"
});
```
4. Save the file
5. Create an xml file with the next contents. Replace `[init-db-file-name.js]` with the name of the file you previously created:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<changeFiles>
    <file path="[init-db-file-name.js]"/>
</changeFiles>
```
6. Put any name to xml file and save it in the same directory where your `js` file is located.
7. Run the program adding the next argument: `--mongeez.location=[/path/load-db.xml]`. Replace `[/path/load-db.xml]` with the full path or relative path and the name of the `xml` file you previously created. You can also put the `xml` and `js` files in the same folder than your `jar`; e.g.:
```bash
$ java -jar employee-1.0.0-RELEASE.jar --mongeez.location=file:///C:/Development/load-init.xml
```
or
```bash
$ java -jar employee-1.0.0-RELEASE.jar --mongeez.location=load-init.xml
```
8. The program should start loading the inserts you defined.

If you want to see an example of the `xml` and `js` files, pleae look at [mongeez.xml](./src/main/resources/mongeez.xml) and [mongeez.js](./src/main/resources/mongeez.js)