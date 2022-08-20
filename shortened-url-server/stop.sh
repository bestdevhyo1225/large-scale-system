#!/bin/bash

# 컨테이너 종료
docker stop shortened-url-server

# 컨테이너 삭제
docker rm shortened-url-server

# 이미지 삭제
# shellcheck disable=SC2046
docker rmi $(docker images --filter=reference="shortened-url-server" -q)

# 네트워크 삭제
docker network rm shortened-url-server_default
