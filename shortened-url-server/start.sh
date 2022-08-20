#!/bin/bash

echo "Create docker image start"

# 이미지 생성
docker build -t shortened-url-server -f shortened-url-server/Dockerfile .

echo "Create docker container start"

# 이미지 실행
docker-compose -f shortened-url-server/docker-compose.yml up -d
