#!/bin/bash

APP_HOST=localhost
APP_PORT=8081

printf "\n\n*********************************************************"
printf "\n        searching on ${APP_HOST} "
printf "\n**********************************************************\n"
curl -v http://$APP_HOST:$APP_PORT/search \
     --header "Content-Type: application/json" \
     --request POST \
     --data '{"page": 0, "criteria" : {"category": "KITES", "country": "EU", "brand": "duotone", "product_name": "evo", "subprod_name": "dlab"} }'
