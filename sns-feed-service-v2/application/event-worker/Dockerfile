FROM openjdk:17-alpine

ENV HOME=/home/sns-feed-service
ENV TZ=Asia/Seoul

EXPOSE 9002

RUN mkdir -p /usr/local/lib/sns-feed-service

COPY build/libs/event-worker-0.0.1-SNAPSHOT.jar /usr/local/lib/sns-feed-service

CMD sh -c "java -jar \
    -Dspring.profiles.active=production \
    -Xms4096m \
    -Xmx4096m \
    -XX:+UseZGC \
    /usr/local/lib/sns-feed-service/event-worker-0.0.1-SNAPSHOT.jar"
