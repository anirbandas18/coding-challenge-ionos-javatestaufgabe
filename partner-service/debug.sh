#!/usr/bin/env zsh
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6003 -jar target/synchronization-app-0.0.1-SNAPSHOT.jar