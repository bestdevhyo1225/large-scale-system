#!/bin/bash

java -jar -Xms4096m -Xmx4096m -XX:+UseZGC -XX:ParallelGCThreads=2 \
-javaagent:/Users/janghyoseog/Documents/pinpoint-apm/pinpoint-agent-2.5.1/pinpoint-bootstrap-2.5.1.jar \
-Dpinpoint.applicationName=sns-feed-query-api \
-Dpinpoint.agentId=sns-feed-query-api \
-Dspring.profiles.active=production \
sns-feed-service-v2/application/query-api/build/libs/query-api-0.0.1-SNAPSHOT.jar
