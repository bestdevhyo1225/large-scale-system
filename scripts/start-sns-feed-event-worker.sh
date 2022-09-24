java -jar -Dspring.profiles.active=prod \
 -javaagent:jmx_exporter/jmx_prometheus_javaagent-0.17.2.jar=9102:jmx_exporter/kafka-consumer.yml \
 -Xms4096m -Xmx4096m -XX:+UseG1GC -XX:ParallelGCThreads=2 \
 sns-feed-service/application/event-worker/feed-server/build/libs/feed-server-0.0.1-SNAPSHOT.jar
