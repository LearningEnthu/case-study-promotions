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
- [Contributing](#contributing)
- [License](#license)

## Introduction

The Case Study Promotions project is designed to handle the processing of promotional data from CSV file.
The application is built with Spring Boot and uses a distributed cache with consistent hashing to efficiently store and retrieve data.
The data is refreshed every 30 minutes.

## Features

- **CSV Data Processing**: Reads promotional data from a CSV file.
- **Distributed Cache**: Uses consistent hashing to distribute data across nodes so that the retrieval is faster even with billions of records

## Setup

- 
