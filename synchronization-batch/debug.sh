#!/usr/bin/env zsh
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6666 -jar target/synchronization-app-2.2.0-SNAPSHOT.jar