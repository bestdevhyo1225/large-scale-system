#!/bin/bash

curl -L -v -d '{"longUrl": "https://test/123"}' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  --url localhost:9000/api/v1/url/shorten &
curl -L -v -d '{"longUrl": "https://test/123"}' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  --url localhost:9000/api/v1/url/shorten &
