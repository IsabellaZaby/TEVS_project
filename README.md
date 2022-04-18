# Distributed System 

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
mvn -pl service-instance spring-boot:run -Dspring-boot.run.arguments="--listening.queue=microservice.one --sending.queue=microservice.two"
```
### Start second service instance 
```bash
mvn -pl service-instance spring-boot:run -Dspring-boot.run.jvmArguments=-Dserver.port=9002 -Dspring-boot.run.arguments="--listening.queue=microservice.two --sending.queue=microservice.one"
```


© Stefan Gass, Isabella Zaby