#!/usr/bin/env zsh
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6444 -jar target/kunde-app-2.5.0-SNAPSHOT.jar