FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt
COPY *.jar .
ENTRYPOINT exec java -Xmx200M -jar *.jar
