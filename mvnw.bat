mvn clean package -DskipTests -e

rem mvn compile jib:build -e

rem mvn clean package -e

rem mvn spring-boot:run -Ddockerfile.skip -e

rem mvn surefire-report:report -e

rem mvn verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -DskipTests -Ddockerfile.skip -e

rem hoverfly.exe -pp 1080 -webserver