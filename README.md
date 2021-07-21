# Life Cycle Assessment Api <a align="right" href="https://ktlint.github.io/"><img align="right" src="https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg" alt="ktlint"></a>

> Carbon footprint calculator for products focused on the food industry 🍔

<p align="center">
  <a href="#installation-and-requirements">Installation and Requirements</a> • 
  <a href="#how-to-run">How to run</a> •  
  <a href="#api-documentation">Api documentation</a> • 
  <a href="#architecture">Architecture</a> • 
  <a href="#license">License</a>
</p>

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development.

## Installation and Requirements

These instructions will get you a copy of the project up and running on your local machine for development. Before we install the application we need these systems and tools configured and installed:

- [JDK version >= 8 and SDK 1.8](https://www.oracle.com/br/java/technologies/javase-downloads.html)
- [Gradle version >= 6.8](https://gradle.org/install/)
- [Aws-cli](https://aws.amazon.com/pt/cli/)
- [Docker](https://docs.docker.com/get-docker/)

It is very easy to install and upload the application. Just follow the steps below and everything will be fine! 🎉

### Application

```
git clone https://github.com/thalees/carbon-footprint-calculator
cd carbon-footprint-calculator
```

Open your favorite IDE and build the project using gradle:
```
./gradlew build
```

### Docker

To run the calculation flow it is necessary to go up and configure the infrastructure and create the queue that will be
used by the application. To mock AWS services we are using localstack and for the database we are using MongoDB. To run 
it, just run the command below:
```
docker-compose up -d
```

After the localstack container has successfully uploaded, create the queue in SQS using the `make` command:
```
make create-queue
```

## How to run

After performing the initial setup, just run the application:
```
./gradlew run
```

Your application will be running on `localhost:8081`

## Api documentation

This project has all its endpoints documented in **Postman** as a **shared collection**, to get a copy of its endpoints, 
just click the link below:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://www.getpostman.com/collections/b318ee28301fb39a1c40)

## Architecture

**_TODO_**

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---
<p align="center"><b>Thanks and good tests 🎉</b></p>
<p align="center">
  <img width="100" height="100" alt="bye" src="https://media.giphy.com/media/JO3FKwP5Fwx44uMfDI/giphy.gif">
</p>