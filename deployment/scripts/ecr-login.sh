#!/usr/bin/env bash
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 836079437595.dkr.ecr.us-east-1.amazonaws.com