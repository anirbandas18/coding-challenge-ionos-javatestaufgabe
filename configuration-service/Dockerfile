FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-configuration-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-configuration-service/configuration-app.jar
EXPOSE 8888
CMD ["java", "-jar",
"-DCONFIGURATION_LOGSTASH_ENABLED=false",
"-DCONFIGURATION_LOGSTASH_HOST=localhost",
"-DCONFIGURATION_LOGSTASH_PORT=4560",
"-DCONFIGURATION_BASE_DIR=/Users/anirban.das",
"/opt/ionos-javatestaufgabe-s3export/s3export-configuration-service/configuration-app.jar"]