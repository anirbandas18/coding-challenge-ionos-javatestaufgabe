#!/usr/bin/env zsh
java -jar -DNAMING_LOGSTASH_ENABLED=true -DLOGBACK_LOGGING_LEVEL=info -DNAMING_LOGSTASH_HOST=localhost -DNAMING_LOGSTASH_PORT=4560 target/naming-app.jar