#!/bin/bash

APP_HOST=find-me-a-kite.herokuapp.com
APP_PORT=80

printf "\n\n*********************************************************"
printf "\n        getting brands for category on ${APP_HOST} "
printf "\n**********************************************************\n"
curl -v http://$APP_HOST:$APP_PORT/brandsInCategory?category=KITES \
     --header "Content-Type: application/json" \
     --request GET
