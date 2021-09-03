FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-naming-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-naming-service/naming-app.jar
EXPOSE 8888
CMD ["java", "-jar",
"-DNAMING_LOGSTASH_ENABLED=${NAMING_LOGSTASH_ENABLED}",
"-DLOGBACK_LOGGING_LEVEL=${LOGBACK_LOGGING_LEVEL}",
"-DNAMING_LOGSTASH_HOST=${NAMING_LOGSTASH_HOST}",
"-DNAMING_LOGSTASH_PORT=${NAMING_LOGSTASH_PORT}",
"/opt/ionos-javatestaufgabe-s3export/s3export-naming-service/naming-app.jar"]