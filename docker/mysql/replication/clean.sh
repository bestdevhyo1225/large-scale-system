# Docker Container Stop
docker stop large-scale-system-write-mysql
docker stop large-scale-system-read-mysql

# Docker Container Remove
docker rm large-scale-system-write-mysql
docker rm large-scale-system-read-mysql

# Docker Image Remove
docker rmi large-scale-system-write-mysql
docker rmi large-scale-system-read-mysql

# Docker Newtork Remove
docker network rm replication_large-scale-system

# Docker Volume Remove All
docker volume prune -f
