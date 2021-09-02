FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-download-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-download-service/download-app.jar
EXPOSE 9001
CMD ["java", "-jar",
"-DSPRING_PROFILES_ACTIVE=local",
"-DSPRING_CLOUD_CONFIG_ENABLED=false",
"-DLOGSTASH_HOST=localhost",
"-DLOGSTASH_PORT=4560",
"-DSPRING_CLOUD_CONFIG_HOST=localhost",
"-DSPRING_CLOUD_CONFIG_PORT=8888",
"-DDOWNLOAD_LOGGING_LEVEL=INFO",
"-DDOWNLOAD_SHOW_SQL=false",
"-DDOWNLOAD_SQL_COMMENTS=false",
"-DDOWNLOAD_FORMAT_SQL=false",
"-DDOWNLOAD_DATABASE_HOST=localhost",
"-DDOWNLOAD_DATABASE_PORT=3306",
"-DDOWNLOAD_EUERKA_HOST=localhost",
"-DDOWNLOAD_EUREKA_PORT=8761",
"-DDOWNLOAD_ZIPKIN_HOST=localhost",
"-DDOWNLOAD_ZIPKIN_PORT=9411",
"-DDOWNLOAD_OBJECT_STORE_HOST=localhost",
"-DDOWNLOAD_OBJECT_STORE_HOST=9000",
"/opt/ionos-javatestaufgabe-s3export/s3export-download-service/download-app.jar"]