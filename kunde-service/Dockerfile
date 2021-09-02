FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-kunde-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-kunde-service/kunde-app.jar
EXPOSE 10001
CMD ["java", "-jar",
"-DSPRING_PROFILES_ACTIVE=local"
"-DSPRING_CLOUD_CONFIG_ENABLED=false",
"-DLOGSTASH_HOST=localhost",
"-DLOGSTASH_PORT=4560",
"-DSPRING_CLOUD_CONFIG_HOST=localhost",
"-DSPRING_CLOUD_CONFIG_PORT=8888",
"-DKUNDE_LOGGING_LEVEL=INFO",
"-DKUNDE_SHOW_SQL=false",
"-DKUNDE_LOGGING_LEVEL=INFO",
"-DKUNDE_SHOW_SQL=false",
"-DKUNDE_SQL_COMMENTS=true",
"-DKUNDE_FORMAT_SQL=true",
"-DKUNDE_DATABASE_HOST=localhost",
"-DKUNDE_DATABASE_PORT=3306",
"-DKUNDE_EUERKA_HOST=localhost",
"-DKUNDE_EUREKA_PORT=8761",
"-DKUNDE_ZIPKIN_HOST=localhost",
"-DKUNDE_ZIPKIN_PORT=9411",
"/opt/ionos-javatestaufgabe-s3export/s3export-kunde-service/kunde-app.jar"]