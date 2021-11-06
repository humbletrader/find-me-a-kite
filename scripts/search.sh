#!/bin/bash

APP_HOST=find-me-a-kite.herokuapp.com
APP_PORT=80

printf "\n\n*********************************************************"
printf "\n        searching on ${APP_HOST} "
printf "\n**********************************************************\n"
curl -v http://$APP_HOST:$APP_PORT/search \
     --header "Content-Type: application/json" \
     --request POST \
     --data '{"category": "KITES", "brand": "Cabrinha", "productName": "Switchblade", "productVersion": "2020", "size": "12", "color" : "yellow", "page": 0}'
