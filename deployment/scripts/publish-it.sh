#!/usr/bin/env bash

cd "${0%/*}"
cd ./../../

mvn -s $JENKINS_HOME/.m2/platform_settings.xml -Dsettings.security=$JENKINS_HOME/.m2/settings-security.xml clean install -DskipTests -Dbuild.number="integration-test"

JAR_NAME=$(ls scheduling-server/target/ | grep SNAPSHOT-jar-with-dependencies.jar)
VERSION=${JAR_NAME/-jar-with-dependencies.jar/}"integration-test"
#aws login
$(aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 836079437595.dkr.ecr.us-east-1.amazonaws.com)

docker push 836079437595.dkr.ecr.us-east-1.amazonaws.com/platform-services:${VERSION}

#deploying  in qa environemnt
pushd deployment/chart/scheduling-service
helm upgrade --install  scheduling-service-qa . -f values_integration_test.yaml  --namespace pt-scheduling-qa --set timestamp="A`date +%s`"  --kube-context eks-develop --tiller-namespace pt-tiller
popd


sleep 90

#running integration tests
pushd scheduling-integration-test
mvn -s $JENKINS_HOME/.m2/platform_settings.xml -Dsettings.security=$JENKINS_HOME/.m2/settings-security.xml -Pqa test -Dmaven.test.skip=false
popd


cp $JENKINS_HOME/.m2/platform_settings.xml deployment/scripts/settings.xml
cp $JENKINS_HOME/.m2/settings-security.xml deployment/scripts/settings-security.xml

#buiilding integration dunmp image
docker build  -f deployment/scripts/IntegrationDump.Dockerfile -t scheduling-server-integration-dump .
docker tag  scheduling-server-integration-dump 836079437595.dkr.ecr.us-east-1.amazonaws.com/platform-services:scheduling-server-integration-dump
docker push 836079437595.dkr.ecr.us-east-1.amazonaws.com/platform-services:scheduling-server-integration-dump

#deploying integration dump and publish sonar job
kubectl config use-context eks-develop
kubectl delete -f deployment/scripts/integration-job.yaml -n pt-scheduling-qa
kubectl create -f deployment/scripts/integration-job.yaml -n pt-scheduling-qa

sleep 180

#getting down qa environment
helm delete --purge scheduling-service-qa --kube-context eks-develop --tiller-namespace pt-tiller

#deploying  in qa environemnt
pushd deployment/chart/scheduling-service
helm upgrade --install  scheduling-service-qa . -f values_qa.yaml  --namespace pt-scheduling-qa --set timestamp="A`date +%s`"  --kube-context eks-develop --tiller-namespace pt-tiller
popd