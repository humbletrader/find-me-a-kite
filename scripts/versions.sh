#!/bin/bash

APP_HOST=find-me-a-kite.herokuapp.com
APP_PORT=80

printf "\n\n*********************************************************"
printf "\n        getting versions for category and brand and name on ${APP_HOST} "
printf "\n**********************************************************\n"
curl -v http://$APP_HOST:$APP_PORT/versionsForNameBrandAndCategory?category=KITES\&brand=duotone\&name=evo \
     --header "Content-Type: application/json" \
     --request GET
