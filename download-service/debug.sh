#!/usr/bin/env zsh
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6333 -jar target/download-app-2.5.0-SNAPSHOT.jar