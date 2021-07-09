# Table of contents
* [Introduction](#Introduction)
* [Quick Start](#Quick-Start)
* [Implementation](#Implementation)
* [Test](#Test)
* [Deployment](#Deployment)
* [Improvements](#Improvements)

# Introduction
In this project, I developed a proof of concept trading platform following the microservices architecture in order to assess the feasibility of replacing the legacy application which employs a hard-to-maintain monolithic architecture. The application has the primary functions to maintain clients and their accounts as well as execute security orders for those clients. More specifically, it can create new traders and accounts, deposit or withdraw funds, display the most up-to-date market data, and save such data to the client's local database for data persistence purposes. The application was developed in Java utilizing the Spring Boot framework for facilitating dependency management across all the microservices. Market data was retrieved from the IEX cloud platform via a REST API using Apache Tomcat as the webservlet container. The relational database management system used was PostgreSQL. Finally, Docker was used for deployment, and two separate containers were created for the database and the application respectively. The containers communicate necessary SQL queries and responses over a local bridge network. Apache Maven was used for building and packaging the app, and GIT was used for version control.

# Quick Start
Prerequisites: Docker, CentOS 7
1. Build database Docker image
```
cd ./springboot/psql
docker build -t trading-psql .  #docker builds ./Dokcerfile by default
docker image ls -f reference=trading-psql
```
2. Build application Docker image
```
cd ./springboot/
docker build -t trading-app . #docker builds ./Dokcerfile by default
docker image ls -f reference=trading-app
```
3. Crete a Docker network
```
#create a new docker network
sudo docker network create trading-net

#verify
docker network ls
```
4. Start Docker containers
```
docker run --name trading-psql-dev \
-e POSTGRES_PASSWORD=password \
-e POSTGRES_DB=jrvstrading \
-e POSTGRES_USER=postgres \
--network trading-net \
-d -p 5432:5432 trading-psql

#Verify
docker ps

#set IEX credential
IEX_PUB_TOKEN="your_token"

#start trading-app container which is attached to the trading-net docker network
docker run -d --rm --name trading-app-dev \
-e "PSQL_URL=jdbc:postgresql://trading-psql-dev:5432/jrvstrading" \
-e "PSQL_USER=postgres" \
-e "PSQL_PASSWORD=password" \
-e "IEX_PUB_TOKEN=${IEX_PUB_TOKEN}" \
--network trading-net \
-p 5000:5000 -t trading-app

#list running containers
#you should see two running docker containers
docker container ls
```
- Try trading-app with SwaggerUI<br/>
  ![alt text](https://raw.githubusercontent.com/jarviscanada/jarvis_data_eng_TomasRotbauer/feature/SBReadme/springboot/assets/TradingApp.png "SwaggerUI in Google Chrome")

# Implemenation
## Architecture
![alt text](https://raw.githubusercontent.com/jarviscanada/jarvis_data_eng_TomasRotbauer/feature/SBReadme/springboot/assets/architecture.png "App Architecture")
### Controller Layer
The controller layer is one layer below the GUI. When a user issues a request via a supported API such as a web browser, the corresponding controller layer method is invoked. The controller layer in turn forwards the request to the corresponding service layer method(s), and returns the information retrieved to the top layer.
### Service Layer 
The service layer is one layer below the controller layer. It contains all the business logic of the application. For example, when deleting a user from the Trader database table, the service layer ensures that they have a balance of zero in their associated account before proceeding. Upon validation, the service layer transfers the request to the data access layer.
### Data Access Layer
The data access objects (DAOs) reside within the data access layer, right below the service layer. The data access layer acts as the interface between the PostgreSQL database, and the IEX Cloud. In general, the DAOs are responsible for performing simple CRUD operations on the database object in question.
### Spring Boot & Apache Tomcat
The application relies on Spring Boot mainly for dependency management across all the layers. The Spring [Boot] framework provides dependency injection through its featured IoC (Inversion of Control) container. The container performs dependency injection upon Bean creation. Spring Boot also provides an embedded webservlet container - Apache Tomcat. Tomcat sits between the HTTP requests and responses, and holds all the necessary logic (including the three layers) to process the requests and responses.
### PostgreSQL & IEX Cloud
The PSQL database featured in this application is used to persist user specific data. For example, it contains user account information and all the user's security orders. The IEX Cloud platform is a REST API that provides market data (e.g. stock quotes). The application acquires its market data from here.

## REST API Usage
### Swagger
Swagger is a set of open-source tools built around the OpenAPI Specification that can help you design, build, document, and consume REST APIs. In this application, Swagger UI is an HTTP client at the top layer, providing a web browser-accessible GUI for the application. It lies between the controller layer and the actual user (see screenshot in [Quick Start](#Quick-Start)).
### Quote Controller
The Quote Controller is a controller-layer component that provides the user with functions for retrieving IEX market data (quotes) as well as caching them to the local PSQL database Quote table. The Quote table stores the ticker, last price, bid price, ask price, bid size, and the ask size. 
#### Endpoints:
- GET `/quote/dailyList`: list all securities that are available for trading in this trading system.
- GET `/quote/iex/ticker/{ticker}`: Retrieve a quote by ticker ID from the IEX Cloud.
- POST `/quote/tickerId/{tickerId}`: Fetch a quote by ticker ID from the IEX Cloud and store it in the local trading system.
- PUT `/quote/`: Manually update a Quote in the Quote table.
- PUT `/quote/iexMarketData`: Update all Quote table quotes using IEX market data.
### Trader/Account Controller
The TraderAccount controller is a controller-layer component that provides the user with functions for managing account and trader information stored in the local PSQL database. The Trader database table stores a client's first/last names, date of birth, country, and email. The Account table stores the trader ID and balance (amount). The amount in the Account table is managed via the controller's withdraw and deposit functions.
#### Endpoints:
- DELETE `/trader/traderId/{traderId}`: Delete a trader from the Trader table.
- POST `/trader/`: Create a new trader and account using a data transfer object (DTO) JSON.
- POST `/trader/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}`: Create a new trader and account.
- PUT `trader/deposit/traderId/{traderId}/amount/{amount}`: Make a deposit into the trader's account.
- PUT `trader/withdraw/traderId/{traderId}/amount/{amount}`: Make a withdrawal from the trader's account.

# Test 
In order to ensure logical and functional correctness of the application, integration tests were written for all components of both the service and data access layers. The testing library used was JUnit 4, and a minimum code coverage of 60% was maintained.
# Deployment
![alt text](https://raw.githubusercontent.com/jarviscanada/jarvis_data_eng_TomasRotbauer/feature/SBReadme/springboot/assets/DockerDiagram.png "Docker Diagram")
* The trading-psql image is based off base image postgres:9.6-alpine. The image contains a special directory called "docker-entrypoint-initdb.d" which is used for storing SQL scripts that are to be executed upon image instantiation. All the necessary .sql scripts were placed into this directory which initialize the database upon container creation.
* The trading-app image holds the application itself. The Dockerfile which assembles the trading-app image contains all the necessary commands to build the application. First, a command imports a Maven image in order to build the application. Next, a command copies over the source code and pom.xml file into the environment, and finally a RUN command issues a regular "mvn clean package" command in order to build the application inside the image.

# Improvements
1. Implement an Order controller so that the user is able to place security orders (This was an optional feature to implement).
2. Add the ability for a trader to own more than one account. Currently, the relationship between the Account and Trader tables is exclusively one-to-one.
3. Implement automatic quote table syncing with the IEX Cloud so that the user need not manually invoke the PUT `/quote/iexMarketData` endpoint everytime.