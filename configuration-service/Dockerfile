FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-configuration-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-configuration-service/configuration-app.jar
EXPOSE 8888
CMD ["java", "-jar",
"-DCONFIGURATION_LOGSTASH_ENABLED=${CONFIGURATION_LOGSTASH_ENABLED}",
"-DLOGBACK_LOGGING_LEVEL=${LOGBACK_LOGGING_LEVEL}",
"-DCONFIGURATION_LOGSTASH_HOST=${CONFIGURATION_LOGSTASH_HOST}",
"-DCONFIGURATION_LOGSTASH_PORT=${CONFIGURATION_LOGSTASH_PORT}",
"-DCONFIGURATION_BASE_DIR=${CONFIGURATION_BASE_DIR}",
"/opt/ionos-javatestaufgabe-s3export/s3export-configuration-service/configuration-app.jar"]