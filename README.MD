# Java Mentoring Program: Java-messaging

This maven-based project contains the exercises defined in the 'java-messaging' module of Java Mentoring program.

0) To run the project one have the following options:

- mvn test. Compiles and runs the tests.
- mvn verify. Compiles, runs the test and generates the following reports: jacoco coverage

1) Pre-requesites for running the application: launch one of the following messaging infrastructures: kafka, rabbitmq or
   activemq.

This project already includes 3 docker-compose.yaml to launch those messaging systems:

- /docker/activemq/docker-compose.yaml
- /docker/kafka/docker-compose.yaml
- /docker/rabbitmq/docker-compose.yaml

For example: `docker-compose up -d`

2) To start the application just run the class App.java. 3 profiles are supported:

- kafka
- rabbitmq
- activemq

for example:
`javac App.java -Dspring.profiles.active=kafka`

3) All the tasks described in the homework are implemented following the guidelines.

- event-messaging-impl. It contains 3 messaging implementations (producers and consumers): kafka, activemq, rabbitmq
- event-service-api. Provided in the homework description, describes the service / messaging methods.
- event-service-dto. Defines the domain.
- event-service-impl. Implementation of the service functionalities.
- event-service-rest. A simple controller to create/udapte/delete events.
