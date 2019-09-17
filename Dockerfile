#FROM ibmcom/websphere-liberty:webProfile7-ubi-min-amd64

FROM ibmcom/websphere-liberty:19.0.0.6-javaee7-ubi-min-amd64

#COPY . /project
#WORKDIR /project

ARG SSL=false

ARG MP_MONITORING=true
ARG HTTP_ENDPOINT=false

RUN mkdir -p /opt/ibm/wlp/usr/shared/config/lib/global


RUN chown -R 1001:0 /opt/ibm/wlp/usr/shared/config/lib/global
RUN chown -R 1001:0 /config

COPY ./src/main/liberty/config/server.xml /config/
COPY ./src/main/liberty/config/server.env /config/
COPY ./src/main/liberty/config/jvm.options /config/
COPY ./target/*.*ar /config/apps/
COPY ./src/main/liberty/lib/* /opt/ibm/wlp/usr/shared/config/lib/global

USER root
RUN configure.sh

USER 1001

# Upgrade to production license if URL to JAR provided
ARG LICENSE_JAR_URL
RUN \
   if [ $LICENSE_JAR_URL ]; then \
     wget $LICENSE_JAR_URL -O /tmp/license.jar \
     && java -jar /tmp/license.jar -acceptLicense /opt/ibm \
     && rm /tmp/license.jar; \
   fi

