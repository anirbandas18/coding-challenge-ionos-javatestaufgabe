FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-auftraege-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-auftraege-service/auftraege-app.jar
EXPOSE 11001
#CMD ["java", "-jar", "-Dspring.profiles.active=${PROFILE`}", "-DLOGGING_LEVEL=${LOGGING_LEVEL}", "-DDATABASE_HOST=${DATABASE_HOST}", "-DDATABASE_PORT=${DATABASE_PORT}", "-DLOGSTASH_ENABLED=${LOGSTASH_ENABLED}", "-DLOGSTASH_HOST=${LOGSTASH_HOST}", "-DLOGSTASH_PORT=${LOGSTASH_PORT}", "/opt/ionos-javatestaufgabe-s3export/s3export-auftraege-service/auftraege-app.jar"]
CMD ["java", "-jar", "/opt/ionos-javatestaufgabe-s3export/s3export-auftraege-service/auftraege-app.jar"]