#!/bin/bash

APP_HOST=find-me-a-kite.herokuapp.com
APP_PORT=80

printf "\n\n*********************************************************"
printf "\n        searching for distinct values on ${APP_HOST} "
printf "\n**********************************************************\n"
curl -v http://$APP_HOST:$APP_PORT/searchDistinctValues \
     --header "Content-Type: application/json" \
     --request POST \
     --data '{"target":"brand","criteria":{"category":{{"value":"KITES","op":"eq"}},"country":{{"value":"EU","op":"eq"}}}}'
