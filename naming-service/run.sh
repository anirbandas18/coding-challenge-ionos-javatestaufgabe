#!/usr/bin/env zsh
java -jar -DNAMING_LOGSTASH_ENABLED=true -DNAMING_LOGSTASH_HOST=localhost -DNAMING_LOGSTASH_PORT=4560 target/naming-app-2.5.4-RELEASE.jar