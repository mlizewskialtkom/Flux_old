#!/bin/bash

# build docker image from file Dockerfile with tag fluxedu-jenkins
docker build -t fluxedu-jenkins .

# run docker image fluxedu withname fluxedu-jenkins, use port 8095, in detach mode
docker run -d -p 8095:8080 --name fluxedu-jenkins fluxedu-jenkins


#otwórz linię komend kontenera jenkinsa
docker exec -it fluxedu-jenkins bash

#wyswietl zawartość pliku /var/jenkins_home/secrets/initialAdminPassword
cat /var/jenkins_home/secrets/initialAdminPassword