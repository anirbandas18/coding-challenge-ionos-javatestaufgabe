#!/usr/bin/env zsh
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6888 -jar target/gateway-app-2.2.0-SNAPSHOT.jar