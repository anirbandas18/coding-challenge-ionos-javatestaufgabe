FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-download-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-download-service/download-app.jar
EXPOSE 9001
CMD ["java", "-jar",
"-DSPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}",
"-DLOGBACK_LOGGING_LEVEL=${LOGBACK_LOGGING_LEVEL}",
"-DSPRING_CLOUD_CONFIG_ENABLED=${SPRING_CLOUD_CONFIG_ENABLED}",
"-DLOGSTASH_HOST=${LOGSTASH_HOST}",
"-DLOGSTASH_PORT=${LOGSTASH_PORT}",
"-DSPRING_CLOUD_CONFIG_HOST=${SPRING_CLOUD_CONFIG_HOST}",
"-DSPRING_CLOUD_CONFIG_PORT=${SPRING_CLOUD_CONFIG_PORT}",
"-DDOWNLOAD_LOGGING_LEVEL=${DOWNLOAD_LOGGING_LEVEL}",
"-DDOWNLOAD_SHOW_SQL=${DOWNLOAD_SHOW_SQL}",
"-DDOWNLOAD_SQL_COMMENTS=${DOWNLOAD_SQL_COMMENTS}",
"-DDOWNLOAD_FORMAT_SQL=${DOWNLOAD_FORMAT_SQL}",
"-DDOWNLOAD_DATABASE_HOST=${DOWNLOAD_DATABASE_HOST}",
"-DDOWNLOAD_DATABASE_PORT=${DOWNLOAD_DATABASE_PORT}",
"-DDOWNLOAD_EUERKA_HOST=${DOWNLOAD_EUERKA_HOST}",
"-DDOWNLOAD_EUREKA_PORT=${DOWNLOAD_EUREKA_PORT}",
"-DDOWNLOAD_ZIPKIN_HOST=${DOWNLOAD_ZIPKIN_HOST}",
"-DDOWNLOAD_ZIPKIN_PORT=${DOWNLOAD_ZIPKIN_PORT}",
"-DDOWNLOAD_OBJECT_STORE_HOST=${DOWNLOAD_OBJECT_STORE_HOST}",
"-DDOWNLOAD_OBJECT_STORE_HOST=${DOWNLOAD_OBJECT_STORE_HOST}",
"/opt/ionos-javatestaufgabe-s3export/s3export-download-service/download-app.jar"]