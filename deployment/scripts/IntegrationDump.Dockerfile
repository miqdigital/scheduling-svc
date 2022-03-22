FROM 836079437595.dkr.ecr.us-east-1.amazonaws.com/commons:maven-3-jdk-17

LABEL maintainer="platform@miqdigital.com"

ADD ./ /usr/src

WORKDIR /usr/src

ENTRYPOINT ["/bin/bash", "-c", "./deployment/scripts/dumpAndPublishSonar.sh"]
