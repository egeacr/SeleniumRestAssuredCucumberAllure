[![Java CI with Maven](https://github.com/egeacr/SeleniumRestAssuredCucumberAllure/actions/workflows/maven.yml/badge.svg)](https://github.com/egeacr/SeleniumRestAssuredCucumberAllure/actions/workflows/maven.yml)

About
The aim of this project is to conduct both WebUI and API tests, utilizing Java, Selenium-RestAssured.

Resources used on that Project :

https://demoqa.com/books
https://api.practicesoftwaretesting.com/brands
Installation
Start by cloning this repository to your local machine
Navigate to the project directory in your terminal
Install dependencies 
Run tests with "mvn -B clean test" 

CI/CD
This repository utilizes GitHub Actions for Continuous Integration (CI). Every push triggers the test suite to maintain codebase integrity. Additionally, tests run three times daily

To manually trigger the workflow:

Navigate to the Actions tab
Initiate the workflow from there

After maven workflow is completed, Github Pages workflow will start and Allure report can be accessable form URL that is displayed from "pages build and development"

