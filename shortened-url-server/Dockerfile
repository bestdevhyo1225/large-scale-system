# Pull base image.
FROM openjdk:11.0.6-jdk

EXPOSE 8080

ENV TZ=Asia/Seoul
ENV HOME=/home/shortened-url-server
ENV JAR=shortened-url-server-0.0.1-SNAPSHOT.jar

RUN useradd shortened-url-server -d $HOME
USER shortened-url-server

WORKDIR $HOME

COPY shortened-url-server/build/libs/$JAR $HOME

CMD sh -c "java -jar \
    -Dspring.profiles.active=prod-docker \
    -Xms4096m \
    -Xmx4096m \
    -XX:+UseG1GC \
    -XX:ParallelGCThreads=2 \
    $HOME/$JAR"
