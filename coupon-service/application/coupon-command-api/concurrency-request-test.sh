#!/bin/bash

curl -L -v -d '{"memberId": 1}' \
  -H "Content-Type: application/json" \
  --url http://localhost:9000/coupons/1/issued &
curl -L -v -d '{"memberId": 1}' \
  -H "Content-Type: application/json" \
  --url http://localhost:9000/coupons/1/issued &
curl -L -v -d '{"memberId": 1}' \
  -H "Content-Type: application/json" \
  --url http://localhost:9000/coupons/1/issued
