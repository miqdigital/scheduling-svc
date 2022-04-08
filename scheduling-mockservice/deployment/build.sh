#!/usr/bin/env bash
IMAGENAME=scheduling-mockservice
echo "Starting build for mockservice with image: ${IMAGENAME}..."
docker build -t ${IMAGENAME} .
echo "Build successful..."