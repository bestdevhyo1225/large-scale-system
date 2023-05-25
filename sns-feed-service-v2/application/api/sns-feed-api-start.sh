#!/bin/bash

java -jar -Xms4096m -Xmx4096m -XX:+UseZGC -XX:ParallelGCThreads=2 \
-javaagent:/Users/janghyoseog/Documents/pinpoint-apm/pinpoint-agent-2.5.1/pinpoint-bootstrap-2.5.1.jar \
-Dpinpoint.applicationName=sns-feed-api \
-Dpinpoint.agentId=sns-feed-api \
-Dspring.profiles.active=production \
sns-feed-service-v2/application/api/build/libs/api-0.0.1-SNAPSHOT.jar
