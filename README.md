# Selenium-AUT-Framework

This is hybrid framework which is combination of modular and data driven framework. This uses TDD(Test driven development) approach.

### Tech stack:
  * Java
  * Selenium webdriver
  * TestNG
  * Maven
  * Extent report 
  * Log4j
  
### Command to run this project
mvn test -DBrowser=Chrome -DEnvironment=DEV

  ## Automation project framework overview
  <img width="906" alt="image" src="https://github.com/rashmi29/fetch-coding-exercise/assets/35393664/7354e049-0c86-4fad-8a19-4e232f299fa9"><br>
  * _pom.xml_ - pom file contains list of dependencies, maven project configuration and integration of testng.xml with maven project.
  * _testng.xml_ - contains list of test cases to be executed
  * _src/main/java/org.fetch.autobase_ - contains initial configuration of browser, driver instance and application url.
  * _src/main/java/org.fetch.utils_ - contains common methods or utilities to work with webelements
  * _src/test/java/org.fetch.or_ - This is object repository. It contains one class one webpage.
  * _src/test/java/org.fetch.businesslogic_ - This is business logic package. It contains business logic for each page.
  * _src/test/java/org.fetch.test_ - This contains test file.
  * _src/test/java/resources/config.properties_ - This file contains url of application.
  * _src/test/java/resources/screenshots_ - All the screenshots are stored in this folder after test execution.
