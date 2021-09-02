FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-naming-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-naming-service/naming-app.jar
EXPOSE 8888
CMD ["java", "-jar",
"-DNAMING_LOGSTASH_ENABLED=true",
"-DNAMING_LOGSTASH_HOST=localhost",
"-DNAMING_LOGSTASH_PORT=4560",
"/opt/ionos-javatestaufgabe-s3export/s3export-naming-service/naming-app.jar"]