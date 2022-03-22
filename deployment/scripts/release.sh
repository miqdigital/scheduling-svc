#!/usr/bin/env bash

cd "${0%/*}"
cd ./../../

# exit if all arguments are supplied or not
if [[ $# -ne 3 ]] ; then
    echo 'Error: Required params gitflow, ReleaseVersion and NextDevelopmentVersion'
    exit 1
fi

# Defining Arguments for the script
gitflow=$1
ReleaseVersion=$2
NextDevelopmentVersion=$3
echo 'Gitflow: ' $gitflow
echo 'ReleaseVersion: ' $ReleaseVersion
echo 'NextDevelopmentVersion: ' $NextDevelopmentVersion

if [ $gitflow = "release-start" ] || [ $gitflow = "release-finish" ]; then
  if [ $gitflow = "release-start" ]; then
        git config user.email jenkins-deployment@mediaiqdigital.com &&
        git config user.name 'Platform-scheduling' &&
        mvn -B gitflow:release-start -Dmaven.javadoc.skip=true -DreleaseVersion=${ReleaseVersion} -DdevelopmentVersion=${NextDevelopmentVersion}-SNAPSHOT -DargLine="-s $JENKINS_HOME/.m2/platform_settings.xml -Dsettings.security=$JENKINS_HOME/.m2/settings-security.xml" &&
        mvn -s $JENKINS_HOME/.m2/platform_settings.xml -Dsettings.security=$JENKINS_HOME/.m2/settings-security.xml process-resources &&
        git push origin HEAD
  else
        git config user.email jenkins-deployment@mediaiqdigital.com &&
        git config user.name 'Platform-scheduling' &&
        mvn -B gitflow:release-finish -Dmaven.javadoc.skip=true -DreleaseVersion=${ReleaseVersion} -DdevelopmentVersion=${NextDevelopmentVersion}-SNAPSHOT -DpostReleaseGoals=deploy -DargLine="-s $JENKINS_HOME/.m2/platform_settings.xml -Dsettings.security=$JENKINS_HOME/.m2/settings-security.xml -DskipTests"
  fi
else
  exit 1
fi
