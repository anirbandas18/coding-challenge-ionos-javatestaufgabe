FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-seed-batch
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-seed-batch/seed-app.jar
EXPOSE 7001
CMD ["java", "-jar",
"-DSPRING_PROFILES_ACTIVE=local",
"-DSPRING_CLOUD_CONFIG_ENABLED=false",
"-DLOGSTASH_HOST=localhost",
"-DLOGSTASH_PORT=4560",
"-DSPRING_CLOUD_CONFIG_HOST=localhost",
"-DSPRING_CLOUD_CONFIG_PORT=8888",
"-DSEED_LOGGING_LEVEL=INFO",
"-DSEED_SHOW_SQL=false",
"-DSEED_SQL_COMMENTS=true",
"-DSEED_FORMAT_SQL=true",
"-DSEED_DATABASE_HOST=localhost",
"-DSEED_DATABASE_PORT=3306",
"-DSEED_EUERKA_HOST=localhost",
"-DSEED_EUREKA_PORT=8761",
"-DSEED_ZIPKIN_HOST=localhost",
"-DSEED_ZIPKIN_PORT=9411",
"-DSEED_GATEWAY_HOST=localhost",
"-DSEED_GATEWAY_PORT=8081",
"-DSEED_CACHE_HOST=localhost",
"-DSEED_CACHE_PORT=6379",
"/opt/ionos-javatestaufgabe-s3export/s3export-seed-batch/seed-app.jar"]