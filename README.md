# Distributed System

## Requirements
The following packages are required for the application to run:
- JDK 11.x (MUST be set as default)
- Maven
- RabbitMQ (service MUST run or being started with the included 'starter.sh' script)
- Screen (only for the 'starter.sh' script)

## Instructions
Start the application automatically by executing the included 'starter.sh' script <u>or</u> start the services manually by entering the following commands after compilation:

### Start discovery server 
```bash
mvn -pl discovery-server spring-boot:run
```
### Start gateway 
```bash
mvn -pl gateway spring-boot:run
```

### Start first service instance 
```bash
mvn -pl service-instance spring-boot:run -Dspring-boot.run.arguments="--listening.queue=microservice.one --sending.queue=microservice.two --sending.two.queue=microservice.three"
```
### Start second service instance 
```bash
mvn -pl service-instance spring-boot:run -Dspring-boot.run.jvmArguments=-Dserver.port=9002 -Dspring-boot.run.arguments="--listening.queue=microservice.two --sending.queue=microservice.one --sending.two.queue=microservice.three"
```

### Start third service instance
```bash
mvn -pl service-instance spring-boot:run -Dspring-boot.run.jvmArguments=-Dserver.port=9003 -Dspring-boot.run.arguments="--listening.queue=microservice.three --sending.queue=microservice.one --sending.two.queue=microservice.two"
```

© Stefan Gass, Isabella Zaby