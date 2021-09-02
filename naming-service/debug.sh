#!/usr/bin/env zsh
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6777 -jar target/naming-app-2.5.0-SNAPSHOT.jar