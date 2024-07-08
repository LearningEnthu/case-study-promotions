# Case Study Promotions

This repository contains a case study for managing and processing promotional data.
It includes a Spring Boot application that processes CSV file containing promotion data with expiry date and provides an endpoint to retrieve the data by ID.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)

## Introduction

The Case Study Promotions project is designed to handle the processing of promotional data from CSV file.
The application is built with Spring Boot and uses a distributed cache with consistent hashing to efficiently store and retrieve data.
The data is refreshed every 30 minutes.

## Features

- **CSV Data Processing**: Reads promotional data from a CSV file.
- **Distributed Cache**: Uses consistent hashing to distribute data across nodes so that the retrieval is faster even with billions of records
- The data is distributed across the Nodes based on the hash of the key.
- Thread Pool here signifies the number of worker nodes in actual distributed environment.

## Setup

- **Requirements**: Java 17, Maven
- **Build the project**: Run `mvn clean install` to build the project and generate the JAR file.

## Running the Application

- **Run the application**: Run `mvn spring-boot:run` to start the application.
- **Access the application**: Open `http://localhost:8080` in your browser.
- Every time the application is started, it reads the data from the CSV file and stores it in the cache.
- The data is refreshed every 30 minutes.

## API Endpoints

- **GET /promotions/{id}**: Retrieves the promotion data by ID.
Example: 
1. From command prompt
- Request: curl -X 'GET' 'http://localhost:8080/promotions/29b25968-e9fa-4b86-b42f-6bd684efe8d0'  -H 'accept: application/json'
- Response: {"id":"29b25968-e9fa-4b86-b42f-6bd684efe8d0","price":76.825372,"expiryDate":"2018-10-06 16:15:16"}
2. From browser
- Request: http://localhost:8080/promotions/29b25968-e9fa-4b86-b42f-6bd684efe8d0
- Response: {"id":"29b25968-e9fa-4b86-b42f-6bd684efe8d0","price":76.825372,"expiryDate":"2018-10-06 16:15:16"}


## Testing

- **Run the tests**: Run `mvn test` to execute the unit tests.

FAQ:
- **Q1. The .csv file could be very big (billions of entries) - how would your application perform? How would you optimize it?**
  - The application is designed to handle large data sets efficiently.
  - It uses a distributed cache with consistent hashing to distribute data across nodes.
  - This ensures that the retrieval is faster even with billions of records. 
    - Let's take an example of 10 billion records, there are approx 70 characters in a record.
      - 140 bytes would be required to save them plus some additional bytes for reference variables.
      - Around 160 bytes for each record. So, 10 billion records would require 1.6 TB of memory.
      - The current system with 15 MB of data the loadData is taking 800 ms to load the data(on single machine, and multiple threads).
      - So, for 1.6 TB of data, we would need 100 Nodes with capacity of 16 GB each.

- **Q2. How would your application perform in peak periods (millions of requests perminute)? How would you optimize it?**
  - The number of workers can be increased to handle the load.
  - The auto up-scaling and down-scaling of the nodes can be done based on the load. And this will be managed by any of the load balancer like Nginx, HAProxy etc.
  - Distributed cache along with frequently accessed data can be stored in memory to reduce the latency.
  - We can have separate read and write databases to handle the load. The read only databases having multiple replicas of each DB node will be able to cater more requests.
  - In this setup the read request is taking around 1ms or less except for first few requests where initialization is happening, here are some example
    - Node: Node0 for id: d018ef0b-dbd9-48f1-ac1a-eb4d90e57118
      Time taken to fetch the record: 29394 microseconds
      Node: Node0 for id: d018ef0b-dbd9-48f1-ac1a-eb4d90e57118
      Time taken to fetch the record: 4092 microseconds
      Node: Node1 for id: dce7d659-f3a5-4418-822d-f544f919a2b9
      Time taken to fetch the record: 390 microseconds
      Node: Node3 for id: 3ff5ae5d-e828-4ebb-b795-05129bf878cd
      Time taken to fetch the record: 1218 microseconds
      Node: Node4 for id: bc5a1d36-e54c-4408-b095-a13355612040
      Time taken to fetch the record: 514 microseconds
      Node: Node1 for id: 29b25968-e9fa-4b86-b42f-6bd684efe8d0
      Time taken to fetch the record: 1750 microseconds

- **Q3. How would you operate this app in production (e.g. deployment, scaling, monitoring)**
    - The application can be deployed on a cloud platform like AWS, Azure, GCP etc. this will reduce the overhead of managing the infrastructure.
    - Auto scaling can also be managed by any of the cloud providers, like AWS Auto Scaling, Azure Auto Scaling service etc.
    - We will deploy the application in containerized environment using Docker and Kubernetes.
    - We can use monitoring tools like Prometheus, Grafana, etc so that we can monitor the application and take necessary actions.
    - proper alerting can be there if the latency is high or the number of requests are high
    - An automated CI/CD pipeline can be set up to deploy the application. So that the deployment is automated and the downtime is reduced.
    - The application can be scaled vertically by increasing the resources of the nodes(higher SKU type with more RAM and cores).
    - The application can be deployed in multiple regions to handle the load and provide high availability.