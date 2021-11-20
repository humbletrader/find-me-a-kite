#!/bin/bash

APP_HOST=find-me-a-kite.herokuapp.com
APP_PORT=80

printf "\n\n*********************************************************"
printf "\n        searching on ${APP_HOST} "
printf "\n**********************************************************\n"
curl -v http://$APP_HOST:$APP_PORT/searchv2 \
     --header "Content-Type: application/json" \
     --request POST \
     --data '{"page": 0, "criteria" : {"category": "KITES", "brand": "Cabrinha", "name": "Switchblade"} }'
