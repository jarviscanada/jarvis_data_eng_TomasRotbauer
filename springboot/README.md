# Table of contents
* [Introduction](#Introduction)
* [Quick Start](#Quick Start)
* [Implementation](#Implementation)
* [Test](#Test)
* [Deployment](#Deployment)
* [Improvements](#Improvements)

# Introduction
In this project, I developed a proof of concept trading platform following the microservices architecture in order to assess the feasibility of replacing the legacy application which employs a hard-to-maintain monolithic architecture. The application has the primary functions to maintain clients and their accounts as well as execute security orders for those clients. More specifically, it can create new traders and accounts, deposit or withdraw funds, display the most up-to-date market data, and save such data to the clients local database for data persistence purposes. The application was developed in Java utilizing the Spring Boot framework for facilitating dependency management across all the microservices. Market data was retrieved from the IEX cloud platform via a REST API using Apache Tomcat as the webservlet container. The relational database management system used was PostgreSQL. Finally, Docker was used for deployment, and two separate containers were created for the database and the application respectively. The containers communicate necessary SQL queries and responses over a local bridge network. Apache Maven was used for building and packaging the app, and GIT was used for version control.

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
- Draw a component diagram that contains controllers, services, DAOs, SQL, IEX Cloud, WebServlet/Tomcat, HTTP client, and SpringBoot. (you must create your own diagram)
- briefly explain the following components and services (3-5 sentences for each)
  - Controller layer (e.g. handles user requests....)
  - Service layer
  - DAO layer
  - SpringBoot: webservlet/TomCat and IoC
  - PSQL and IEX

## REST API Usage
### Swagger
What's swagger (1-2 sentences, you can copy from swagger docs). Why are we using it or who will benefit from it?
### Quote Controller
- High-level description for this controller. Where is market data coming from (IEX) and how did you cache the quote data (PSQL). Briefly talk about data from within your app
- briefly explain each endpoint
  e.g.
  - GET `/quote/dailyList`: list all securities that are available to trading in this trading system blah..blah..
### Trader Controller
- High-level description for trader controller (e.g. it can manage trader and account information. it can deposit and withdraw fund from a given account)
- briefly explain each endpoint
### Order Controller
- High-level description for this controller.
- briefly explain each endpoint
### App controller
- briefly explain each endpoint
### Optional(Dashboard controller)
- High-level description for this controller.
- briefly explain each endpoint

# Test 
How did you test your application? Did you use any testing libraries? What's the code coverage?

# Deployment
- docker diagram including images, containers, network, and docker hub
e.g. https://www.notion.so/jarviscanada/Dockerize-Trading-App-fc8c8f4167ad46089099fd0d31e3855d#6f8912f9438e4e61b91fe57f8ef896e0
- describe each image in details (e.g. how psql initialize tables)

# Improvements
If you have more time, what would you improve?
- at least 3 improvements