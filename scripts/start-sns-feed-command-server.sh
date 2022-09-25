java -jar -Dspring.profiles.active=prod \
  -javaagent:jmx_exporter/jmx_prometheus_javaagent-0.17.2.jar=9100:jmx_exporter/kafka-producer.yml \
  -Xms4096m -Xmx4096m -XX:+UseG1GC -XX:ParallelGCThreads=2 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/headdump/heapdump.hprof \
  sns-feed-service/application/api/command-server/build/libs/command-server-0.0.1-SNAPSHOT.jar
