#!/bin/bash

# @author: Stefan Gass, Isabella Zaby
# Version 1.0., April 2022

path=$(dirname "$0")

echo "Welcome! :-)"
echo "This scripts acts as a starter for the included spring services."
echo
echo "For it to work, the following packages need to be installed:"
echo "JDK 11.x (MUST be set as default)"
echo "Maven"
echo "RabbitMQ (service MUST run or being started with script)"
echo "Screen"
if [[ "$OSTYPE" != "linux-gnu"* ]]; then
  echo
  echo "ATTENTION: Make sure to terminate all running screen sessions before restarting them with this script!"
fi
echo
echo "Do you want to continue? (y/n)"
read -r continue
if [[ $continue != y* ]]; then
  echo "Bye!"
  exit 0
fi

# Works only on Linux...
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    screen -X -S rabbitMQ kill &>/dev/null
    screen -X -S discoveryServer kill &>/dev/null
    screen -X -S gateway kill &>/dev/null
    screen -X -S serviceOne kill &>/dev/null
    screen -X -S serviceTwo kill &>/dev/null
    screen -X -S serviceThree kill &>/dev/null
    echo
    echo "Shut down running screen session."
fi

cd "$path" || exit 1

# Reset data
test -f ./service-instance/src/main/resources/data9001.json && cat /dev/null > ./service-instance/src/main/resources/data9001.json
test -f ./service-instance/src/main/resources/data9002.json && cat /dev/null > ./service-instance/src/main/resources/data9002.json
test -f ./service-instance/src/main/resources/data9003.json && cat /dev/null > ./service-instance/src/main/resources/data9003.json

echo
echo "Do you want to start the RabbitMQ server? (y/n)"
read -r continueMQ
if [[ $continueMQ == y* ]]; then
  screen -AmdS rabbitMQ rabbitmq-server
fi

echo
echo "Do you want to recompile every service before starting it? (y/n)"
echo "ATTENTION: This may take a while, so make sure to wait until the script is finished!"
read -r continueRC
if [[ $continueRC == y* ]]; then
  mvn clean install spring-boot:repackage
  cd ./discovery-server || exit 1
  mvn clean install spring-boot:repackage
  cd ../gateway || exit 1
  mvn clean install spring-boot:repackage
  cd ../service-instance || exit 1
  mvn clean install spring-boot:repackage
  cd ..
fi

cd ./discovery-server || exit 1
screen -AmdS discoveryServer mvn spring-boot:run
cd ../gateway || exit 1
screen -AmdS gateway mvn spring-boot:run
cd ../service-instance || exit 1
screen -AmdS serviceOne mvn spring-boot:run -Dspring-boot.run.arguments="--listening.queue=microservice.one --sending.queue=microservice.two --sending.two.queue=microservice.three"
screen -AmdS serviceTwo mvn spring-boot:run -Dspring-boot.run.jvmArguments=-Dserver.port=9002 -Dspring-boot.run.arguments="--listening.queue=microservice.two --sending.queue=microservice.one --sending.two.queue=microservice.three"
screen -AmdS serviceThree mvn spring-boot:run -Dspring-boot.run.jvmArguments=-Dserver.port=9003 -Dspring-boot.run.arguments="--listening.queue=microservice.three --sending.queue=microservice.one --sending.two.queue=microservice.two"

echo
screen -ls
echo
echo "To access a running screen session enter \"screen -r session-name\""
echo "To leave a session without terminating the running service, enter CTRL+a+d inside."

echo
echo "Do you want to start the client application? (y/n)"
read -r continueCl
if [[ $continueCl == y* ]]; then
  open ../index.html
  echo
  echo "Client started in browser."
  echo "ATTENTION: You have to wait a few seconds, until all the services were started."
fi

echo
echo "Bye!"

exit 0
