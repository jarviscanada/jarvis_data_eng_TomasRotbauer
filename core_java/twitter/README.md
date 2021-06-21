# Introduction
This Java application allows users to post, show, and delete tweets on their Twitter account all from the command line. The application works with the Twitter REST API, formulating all HTTP requests accordingly. The HTTP requests are sent using the Java/Apache HTTP client package. Because the application involves multiple component layers as it conforms to the Model-View-Controller (MVC) architecture, dependency management was handled with Spring Boot. Other tools and software that were used include the Maven build tool, Docker for deployment, and GIT version control.

# Quick Start
- Compile, build & package with Maven: `mvn clean package`
- Run with Docker: 
  ```
  docker run --rm \
  -e consumerKey=YOUR_VALUE \
  -e consumerSecret=YOURVALUE \
  -e accessToken=YOUR_VALUE \
  -e tokenSecret=YOUR_VALUE \
  tomasrotbauer/twitter_app <post | show | delete> [OPTIONS]
  ```

# Design
The application was built based on the MVC architecture. It comprises a model (M), no view in this case (V), and a controller (C) that is subdivided into four hierarchical, interdependent layers. Each layer is described in detail below.
## UML diagram
![alt text]( https://raw.githubusercontent.com/jarviscanada/jarvis_data_eng_TomasRotbauer/feature/twitterReadme/core_java/twitter/assets/twitter.png "Twitter App UML Diagram")
## Components
### Application (Top Layer)
The top layer is responsible for reading and parsing user inputs from the command line (passed in as program parameters). The program checks if the user has provided one of the three valid commands, and passes the instruction down to the controller layer.
### Controller
The controller layer further validates the instruction by checking for any required and optional parameters. If the instruction is complete and valid, the controller calls the respective service layer function with the corresponding method arguments.
### Service
Now that the instruction is converted into an internal representation, the service layer can focus on business logic. For instance, it ensures that a post does not exceed 140 characters. It then provides the corresponding payload to one of the DAO CRUD methods.
### DAO
The DAO layer is responsible for sending HTTP requests and receiving responses from the Twitter REST server. It also handles unexpected server replies in case of errors. The final response is parsed into a JSON string and returned to the top layer.

## Models
The application primarily relies on a Tweet object model since tweets are primarily what the application needs to handle. The Tweet object contains multiple fields such as ID, text, creation date, and other attributes that a typical tweet has. It also contains a custom Coordinates class for holding the longitude/latitude values of the corresponding Tweet location, and also an Entities class that holds hashtags and user mentions. Both hashtags and user mentions are separate classes with the necessary fields to hold all required information.
## Spring
This application implements many interdependent layers. As such, many dependencies exist, where one class object relies on the existence of another class object and so on. To manage dependencies automatically, the Spring framework was applied with Spring Boot which injects dependencies automatically based on simple class annotations. In this application, the following are Spring components:
* TwitterCLIApp
* Controller
* Service
* CrdDao
* HttpHelper
# Test
Testing was done with JUnit 4 and Mockito frameworks. Unit and integration tests were written for each individual layer to ensure correct behaviour, and successive layers were developed only after the dependency layers beneath it were fully tested and proven to be functional. With this bottom-up development strategy, bugs were always conveniently isolated only to the layer under development. Testing was mainly based on verifying correctness of fetched information and ensuring that malformed user commands were rejected. Unit tests made use of Mock objects to simulate behaviour of any dependencies, thereby isolating the components of interest.
## Deployment
In order to facilitate distribution, the final product was packaged into a Docker image named tomasrotbauer/twitter_app. A simple Dockerfile was written which builds the image from the openjdk:8-alpine image and copies over the compiled jar file. To run the container after pulling it from Docker Hub, execute:
  ```
  docker run --rm \
  -e consumerKey=YOUR_VALUE \
  -e consumerSecret=YOURVALUE \
  -e accessToken=YOUR_VALUE \
  -e tokenSecret=YOUR_VALUE \
  tomasrotbauer/twitter_app <post | show | delete> [OPTIONS]
  ```
The entrypoint is already set to launch the application, hence include the desired command directly as shown above.

# Improvements
- Add other features such as direct messaging, following other users, editing user profile, etc.
- Implement a better way of specifying which tweet to show/delete, because acquiring the tweet ID is impractical
- Implement a read-eval-print (REPL) loop so that the user does not need to relaunch the application with all the keys and secrets everytime they need to perform a single action