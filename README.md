# Java Web Application for Secure Employee Data Management:
A java based application that manages details about the employees working in an Organization.
This project works with the concept of Layered Programming. It consists of three layers.<br>
<b>Data Layer (DL):</b> 
It deals with file handling techniques to read the data from the file and perform CURD operations .<br>
<b>Buisness Layer (BL):</b> 
It deals with data structures to organize the data and fetch data at a faster rate, rather than finding them and reading them from file every time.<br>
<b>Presentation Layer (PL):</b>
At This layer, the view of the application is made for the user, and the GUI is created using Java Swing and AWT.
<br><br>
## Software Specification
```bash
C:\>java -version
java version "20.0.2" 2023-07-18
Java(TM) SE Runtime Environment (build 20.0.2+9-78)
Java HotSpot(TM) 64-Bit Server VM (build 20.0.2+9-78, mixed mode, sharing)

C:\>gradle -version

------------------------------------------------------------
Gradle 8.2.1
------------------------------------------------------------

Build time:   2023-07-10 12:12:35 UTC

Kotlin:       1.8.20
Groovy:       3.0.17
Ant:          Apache Ant(TM) version 1.10.13 compiled on January 4 2023
JVM:          20.0.2 (Oracle Corporation 20.0.2+9-78)
OS:           Windows 11 10.0 amd64

```


## Folders Abbreviations
<ul>
  <li>DL: Data Layer (File Handling Technique)</li>
  <li>DBDL: Database Data Layer (Database Technique)</li>
  <li>PL: Presentation Layer</li>
  <li>BL: Business Layer </li>
</ul>

## Common
This folder includes files that will be shared between all the layers like **enums** <br>
To compile GENDER.java <br>
```bash
HR\common\src> javac -d ..\classes -classpath;. com\employee\management\enums\*.java
```
To create a jar file of **common** folder: <br>
```bash
HR\common\classes> jar -cvf ..\dist\hr-common.jar com
```
The above line will create a **hr-common.jar** file.


## DL: Data Layer
To compile the following files: DAOException.java <br>
```bash
HR\DL\src> javac -d ..\classes -classpath ..\..\common\dist\hr-common.jar;. com\employee\management\hr\dl\exceptions\*.java
```
To compile the following files: DesignationDTOInterface.java, EmployeeDTOInterface.java <br>
```bash
HR\DL\src> javac -d ..\classes -classpath ..\..\common\dist\hr-common.jar;. com\employee\management\hr\dl\interfaces\dto\*.java
```
To compile the following files: DesignationDAOInterface.java, EmployeeDAOInterface.java <br>
```bash
HR\DL\src> javac -d ..\classes -classpath ..\..\common\dist\hr-common.jar;. com\employee\management\hr\dl\interfaces\dto\*.java
```
To compile the following files: DesignationDTO.java, EmployeeDTO.java <br>
```bash
HR\DL\src> javac -d ..\classes -classpath ..\..\common\dist\hr-common.jar;. com\employee\management\hr\dl\dto\*.java
```
To compile the following files: DesignationDAO.java, EmployeeDAO.java <br>
```bash
HR\DL\src> javac -d ..\classes -classpath ..\..\common\dist\hr-common.jar;. com\employee\management\hr\dl\dao\*.java
```
To compile **Testcases** <br>
```bash
HR\DL\testcases> javac -classpath ..\..\common\dist\hr-common.jar;..\classes;. *.java
```

## Business Layer
To compile files:
```bash
HR\BL>gradle build
```

## Presentation Layer
To compile files:
```bash
HR\PL>gradle build
```
**To Run the application:**
```bash
HR\PL>java -classpath ..\common\dist\hr-common.jar;..\DL\dist\hr-dl-1.0.jar;..\BL\build\libs\BL.jar;build\libs\PL.jar;. com.employee.management.hr.pl.Main
```
