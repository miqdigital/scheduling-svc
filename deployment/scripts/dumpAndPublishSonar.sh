#!/usr/bin/env bash

mvn -s /usr/src/deployment/scripts/settings.xml -Dsettings.security=/usr/src/deployment/scripts/settings-security.xml clean install -DskipTests -Ddockerfile.skip=true

#gettting results
mvn -s /usr/src/deployment/scripts/settings.xml -Dsettings.security=/usr/src/deployment/scripts/settings-security.xml -Pcoverage jacoco:dump@post-integration-test -Dapp.host=scheduling-service-qa.pt-scheduling-qa.svc -Dapp.port=36320 -Dskip.dump=false
mvn -s /usr/src/deployment/scripts/settings.xml -Dsettings.security=/usr/src/deployment/scripts/settings-security.xml -Pcoverage jacoco:merge@merge-test-data

#mvn -s /usr/src/deployment/scripts/settings.xml -Dsettings.security=/usr/src/deployment/scripts/settings-security.xml sonar:sonar -Dsonar.host.url=https://sonar.dev.miqdigital.com/sonar-it/ -Dsonar.login=miquser -Dsonar.password=Son@r@4567 -Dsonar.analysis.mode=publish

mvn -s /usr/src/deployment/scripts/settings.xml -Dsettings.security=/usr/src/deployment/scripts/settings-security.xml sonar:sonar -Dsonar.host.url=https://sonar.dev.miqdigital.com/sonar-it/ -Dsonar.junit.reportPaths=target/surefire-reports -Dsonar.java.coveragePlugin=jacoco  -Dsonar.inclusions=**/*.java -Dsonar.java.binaries=**/classes/** -Dsonar.login=miquser -Dsonar.password=Son@r@4567 -Dsonar.analysis.mode=publish
