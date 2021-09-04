java -jar -DSPRING_PROFILES_ACTIVE=development -DLOGBACK_LOGGING_LEVEL=info -DSPRING_CLOUD_CONFIG_ENABLED=true -DLOGSTASH_HOST=localhost -DLOGSTASH_PORT=4560 -DSPRING_CLOUD_CONFIG_HOST=localhost -DSPRING_CLOUD_CONFIG_PORT=8888 -DSYNC_LOGGING_LEVEL=INFO -DSYNC_SHOW_SQL=false -DSYNC_SQL_COMMENTS=true -DSYNC_FORMAT_SQL=true -DSYNC_DATABASE_HOST=localhost -DSYNC_DATABASE_PORT=3306 -DSYNC_EUERKA_HOST=localhost -DSYNC_EUREKA_PORT=8761 -DSYNC_ZIPKIN_HOST=localhost -DSYNC_ZIPKIN_PORT=9411 -DSYNC_GATEWAY_HOST=localhost -DSYNC_GATEWAY_PORT=8081 -DSYNC_CACHE_HOST=localhost -DSYNC_CACHE_PORT=6379 -DSYNC_OBJECT_STORE_HOST=localhost -DSYNC_OBJECT_STORE_PORT=9000 target\synchronization-app.jar