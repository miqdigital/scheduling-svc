#!/usr/bin/env bash
VERSION=$1
IMAGENAME=836079437595.dkr.ecr.us-east-1.amazonaws.com/platform-services:scheduling-mockservices-${VERSION}
echo "Starting build for mockservice with image: ${IMAGENAME}..."
docker build -t ${IMAGENAME} .
echo "Build successfull..."