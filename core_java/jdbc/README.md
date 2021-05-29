# Introduction
In this project, I developed a simple data access object (DAO) application using Java Database Connectivity (JDBC) API for accessing and running queries on a simple database with a couple of tables. The application can perform all the fundamental CRUD (create, read, update & delete) operations on the local database via transactions. The database itself is managed by PostgreSQL RDBMS, and runs inside a Docker container to avoid local installation. The entire project is built and managed by Maven.

# Implementation
Below is a high level overview of the implementation of the application. The entity-relationship (ER) diagram is shown below, and illustrates the database schema. Under the Design Patterns section, the DAO pattern is compared to the Repository pattern to illustrate how the application interacts with the database.
## ER Diagram
![alt text](https://raw.githubusercontent.com/jarviscanada/jarvis_data_eng_TomasRotbauer/develop/core_java/jdbc/assets/ER.png "Database Schema")

## Design Patterns
There are primarily two patterns that can be implemented in order to facilitate accessing a database from an application running with JDBC. The data access object (DAO), or Repository pattern. Both have the same purpose to abstract the persistence layer access details, but are subtly different. In this application, the DAO pattern was used.

### DAO
A DAO is a per-table object that provides CRUD functionality by executing transactions against a given database. A DAO is data-centric, and lies close to the underlying storage. This means that, for instance, a DAO will perform joins with the help of the DBMS by executing a query. A DAO also makes use of data transfer objects (DTOs), which are direct representations of the database tables in code.

### Repository
A repository, just like the DAO, hides queries and manages data. In fact, a repository pattern often makes use of DAOs because they are similar. However, repositories are a layer above the DAO pattern, since the repository is more abstracted from the underlying storage, and closer to the business logic. For instance, the repository pattern might perform a table join in code instead of the DBMS, only extracting the necessary data using a DAO.

# Test
Testing the JDBC app was very simple, mainly involving manual verification of changes having taken place inside the database after the execution of a given transaction. Below is a short list of the general procedures taken towards testing the application:
* To ensure that the database was set up correctly and running, a simple login was performed from the shell: `psql -h localhost -U postgres`
* To set up test data, SQL scripts were written that created and populated the tables of the database
* To test query results (i.e. read operations), the same queries were executed inside the database instance and compared to the results of the JDBC application. The same was done for write operations (including updates, inserts and deletions), where the database was checked for the new modifications
