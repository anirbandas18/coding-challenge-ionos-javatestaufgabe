#!/usr/bin/env zsh

mvn clean package -DskipTests -e && mvn compile com.google.cloud.tools:jib-maven-plugin:build -DskipTests -e
# mvn clean package -e \
# mvn spring-boot:run -Ddockerfile.skip -e \
# mvn surefire-report:report -e \
# mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -DskipTests -Ddockerfile.skip -e \
# hoverfly.exe -pp 1080 -webserver \