#!/usr/bin/env zsh
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6111 -jar target/auftraege-app-2.2.0-SNAPSHOT.jar