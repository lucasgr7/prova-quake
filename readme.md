# Logs Quake 3 Arena
[![CircleCI](https://circleci.com/gh/lucasgr7/prova-quake.svg?style=svg)](https://circleci.com/gh/lucasgr7/prova-quake)

API for the Quake 3 Logs.
[Live demo](https://prova-quake.herokuapp.com/ "App hosted on heroku")

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites


```
Java >= 11.0.3
Mvn 3.6.1
```
[Java 11 download](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
[Maven download](https://maven.apache.org/download.cgi)


### Main porpouse

This project shall read the `games.log` in the root access of the project.
You can change the path to the log changing the enviroment variable `


### Installing

First, clone this repository into your machine

```
git clone https://github.com/lucasgr7/prova-quake.git
```

Maven execute the app

```
mvn spring-boot:run
```

Default port for the project is `8080`

Access http://localhost8080 you shall see the swagger page for the project



## Running the tests

I'm covering the tests using **Junit 4**.
You can find then in the folder

`/src/test/`

## Deployment

Hosting the project into heroku, here is the link to the app:
https://prova-quake.herokuapp.com/

## Built With

* [Spring boot](https://spring.io/projects/spring-boot) - ❤️ Spring
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Lucas Ribeiro** - *All development* - [My portfolio of projects](https://github.com/lucasgr7?tab=repositories)

## Acknowledgments

* Coded using best pratices i know, hope is enough
* Used SonarQube to remove code smells
* Use [this discussion](https://stackoverflow.com/questions/286846/describe-the-architecture-you-use-for-java-web-applications) to determine how to structure the project
