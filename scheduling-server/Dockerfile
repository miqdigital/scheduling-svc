FROM openjdk:17-alpine

LABEL maintainer="platform@miqdigital.com"

RUN apt-get --no-install-recommends update && apt-get --no-install-recommends install -y \
    curl \
    unzip \
    && rm -rf /var/lib/apt/lists/*

#This should contain JVM tuning options like -Xms2g or/and -Xmx2g
ENV JAVA_OPTS="-XX:MinRAMPercentage=40.00 -XX:MaxRAMPercentage=80.00 -XX:InitialRAMPercentage=10.00"

RUN mkdir /opt/apps

ARG JAR_FILE
COPY target/${JAR_FILE} /opt/apps/app.jar

WORKDIR /opt/apps

EXPOSE 8080 1234


CMD ["exec", "java","${JAVA_OPTS}","-jar", "app.jar"]
