#!/bin/bash

java -jar -Xms4096m -Xmx4096m -XX:+UseZGC -XX:ParallelGCThreads=2 \
-Dspring.profiles.active=production \
sns-feed-service-v2/application/event-worker/build/libs/event-worker-0.0.1-SNAPSHOT.jar
