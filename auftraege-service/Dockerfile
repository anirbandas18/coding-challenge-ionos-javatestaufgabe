FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-auftraege-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-auftraege-service/auftraege-app.jar
EXPOSE 11001
CMD ["java", "-jar",
"-DSPRING_PROFILES_ACTIVE=local",
"-DSPRING_CLOUD_CONFIG_ENABLED=false",
"-DLOGSTASH_HOST=localhost",
"-DLOGSTASH_PORT=4560",
"-DSPRING_CLOUD_CONFIG_HOST=localhost",
"-DSPRING_CLOUD_CONFIG_PORT=8888",
"-DAUFTRAEGE_LOGGING_LEVEL=INFO",
"-DAUFTRAEGE_SQL_COMMENTS=false",
"-DAUFTRAEGE_SHOW_SQL=false",
"-DAUFTRAEGE_FORMAT_SQL=false",
"-DAUFTRAEGE_DATABASE_HOST=localhost",
"-DAUFTRAEGE_DATABASE_PORT=3306",
"-DAUFTRAEGE_EUERKA_HOST=localhost",
"-DAUFTRAEGE_EUREKA_PORT=8761",
"-DAUFTRAEGE_ZIPKIN_HOST=localhost",
"-DAUFTRAEGE_ZIPKIN_PORT=9411",
"/opt/ionos-javatestaufgabe-s3export/s3export-auftraege-service/auftraege-app.jar"]