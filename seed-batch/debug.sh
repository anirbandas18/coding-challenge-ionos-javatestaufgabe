#!/usr/bin/env zsh
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:6002 -jar target/seed-app-1.0.0-RELEASE.jar