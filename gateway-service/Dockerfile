FROM adoptopenjdk:11-jre-hotspot-focal
MAINTAINER Anirban Das <anirbandas18@live.com>
RUN mkdir ~p -p /opt/ionos-javatestaufgabe-s3export/s3export-gateway-service
COPY target/*.jar /opt/ionos-javatestaufgabe-s3export/s3export-gateway-service/gateway-app.jar
EXPOSE 8888
CMD ["java", "-jar",
"-DSPRING_PROFILES_ACTIVE=development",
"-DSPRING_CLOUD_CONFIG_ENABLED=true",
"-DLOGSTASH_ENABLED=false",
"-DLOGSTASH_HOST=localhost",
"-DLOGSTASH_PORT=4560",
"-DSPRING_CLOUD_CONFIG_HOST=localhost",
"-DSPRING_CLOUD_CONFIG_PORT=8888",
"-DGATEWAY_LOGGING_LEVEL=INFO",
"-DGATEWAY_EUERKA_HOST=localhost",
"-DGATEWAY_EUREKA_PORT=8761",
"-DGATEWAY_ZIPKIN_HOST=localhost",
"-DGATEWAY_ZIPKIN_PORT=9411",
"/opt/ionos-javatestaufgabe-s3export/s3export-gateway-service/gateway-app.jar"]
