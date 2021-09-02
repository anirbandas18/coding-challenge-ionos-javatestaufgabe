#!/usr/bin/env zsh
java -jar -DSPRING_PROFILES_ACTIVE=development -DSPRING_CLOUD_CONFIG_ENABLED=true -DLOGSTASH_HOST=localhost -DLOGSTASH_PORT=4560 -DSPRING_CLOUD_CONFIG_HOST=localhost -DSPRING_CLOUD_CONFIG_PORT=8888 -DAUFTRAEGE_LOGGING_LEVEL=INFO -DAUFTRAEGE_SQL_COMMENTS=false -DAUFTRAEGE_SHOW_SQL=false -DAUFTRAEGE_FORMAT_SQL=false -DAUFTRAEGE_DATABASE_HOST=localhost -DAUFTRAEGE_DATABASE_PORT=3306 -DAUFTRAEGE_EUERKA_HOST=localhost -DAUFTRAEGE_EUREKA_PORT=8761 -DAUFTRAEGE_ZIPKIN_HOST=localhost -DAUFTRAEGE_ZIPKIN_PORT=9411 target/auftraege-app-2.5.0-SNAPSHOT.jar