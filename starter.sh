#!/bin/bash

# @author: Stefan Gass, Isabella Zaby
# Version 1.0., April 2022

path=$(dirname "$0")

echo "Welcome! :)"
echo "This scripts acts as a starter for the included spring services."
echo
echo "For it to work, the following packages need to be installed:"
echo "JDK 11.x (MUST be set as default)"
echo "Maven"
echo "RabbitMQ (service MUST run)"
echo "Screen"
echo
echo "ATTENTION: Make sure to terminate all running screen sessions before restarting them with this script!"
echo
echo "Do you want to continue? (y/n)"

read continue
if [[ $continue != y* ]]; then
    echo "Bye!"
    exit 0
fi

# Works only on Linux...
#screen -X -S discoveryServer quit &> /dev/null
#screen -X -S gateway quit &> /dev/null
#screen -X -S serviceOne quit &> /dev/null
#screen -X -S serviceTwo quit &> /dev/null

cd $path/discovery-server
screen -AmdS discoveryServer mvn spring-boot:run
cd ../gateway
screen -AmdS gateway mvn spring-boot:run
cd ../service-instance
screen -AmdS serviceOne mvn spring-boot:run -Dspring-boot.run.arguments="--listening.queue=microservice.one --sending.queue=microservice.two"
screen -AmdS serviceTwo mvn spring-boot:run -Dspring-boot.run.jvmArguments=-Dserver.port=9002 -Dspring-boot.run.arguments="--listening.queue=microservice.two --sending.queue=microservice.one"

echo
screen -ls
echo
echo "To access a running screen session enter \"screen -r session-name\""
echo "To leave a session without terminating the running service, enter CTRL+a+d inside."
echo
echo "Bye!"

exit 0