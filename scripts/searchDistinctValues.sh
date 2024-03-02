#!/bin/bash

APP_HOST=localhost
APP_PORT=8081

printf "\n\n*********************************************************"
printf "\n        searching for distinct values on ${APP_HOST} "
printf "\n**********************************************************\n"
curl -v http://$APP_HOST:$APP_PORT/searchDistinctValues \
     --header "Content-Type: application/json" \
     --request POST \
     --data '{ "target" : "subprod_name", "criteria" : {"category": "KITES", "country": "EU", "brand": "duotone"}}'
