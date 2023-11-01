# Company Scraping Project
This is my web application for scraping information from a list of websites and storing data using Elasticsearch and Excel csv files.

## Project Structure
The project consists of the following main components:
- `WebConfig`: Configuration class for setting up Spring Web MVC and configuring resource handling.
- `CompanyController`: RESTful API controller for managing company data. It includes endpoints for finding all companies and inserting new companies.
- `Company`: A Java class representing the Company entity, with Elasticsearch document annotations.
- `CompanyRepo`: Elasticsearch repository for CRUD operations on Company entities.
- `CompanyService`: Service class responsible for interacting with the Elasticsearch repository and performing CRUD operations on Company entities.
- `ScraperService`: Service class responsible for scraping data from websites and saving it to Elasticsearch.
- `index.html`: HTML file for the web application's front end. It includes buttons for starting the scraping process and searching for companies.

## Captures
![Screenshot 2023-11-01 151852](https://github.com/robuconstantinrobert/WebscraperProject/assets/101166776/58b52604-ce3a-4c6c-9657-5b9f610ba2bf)
![Screenshot 2023-11-01 151829](https://github.com/robuconstantinrobert/WebscraperProject/assets/101166776/6b7e5208-43c0-474b-bdef-fb6d2d2bcfc5)
![Screenshot 2023-11-01 140729](https://github.com/robuconstantinrobert/WebscraperProject/assets/101166776/17870ff8-40b3-4ef6-8ea7-c2f76cda58ab)
![projectWebScraper1](https://github.com/robuconstantinrobert/WebscraperProject/assets/101166776/59984bd7-6f21-448f-9e1e-f734bd696647)
![Screenshot 2023-11-01 152206](https://github.com/robuconstantinrobert/WebscraperProject/assets/101166776/457acf71-17dc-4cb4-b42c-4ac81b26412d)
![Screenshot 2023-11-01 151921](https://github.com/robuconstantinrobert/WebscraperProject/assets/101166776/255c39c2-a74d-42b2-a2ae-c38ae0ef3bfa)
