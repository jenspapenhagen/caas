FROM base/java

LABEL maintainer="Jens Papenhagen" description="Java EE8 Full & Web Distribution"
ENV VERSION 14.0.1.Final
ENV INSTALL_DIR /opt
ENV WILDFLY_HOME ${INSTALL_DIR}/wildfly-${VERSION}
ENV LOG_DIR ${WILDFLY_HOME}/standalone/log/
ENV DEPLOYMENT_DIR ${WILDFLY_HOME}/standalone/deployments/
ENV CONFIGURATION_DIR ${WILDFLY_HOME}/standalone/configuration
RUN useradd -b /opt -m -s /bin/sh -d ${INSTALL_DIR} serveradmin && echo serveradmin:serveradmin | chpasswd
RUN curl -O https://download.jboss.org/wildfly/${VERSION}/wildfly-${VERSION}.zip \
    && unzip wildfly-${VERSION}.zip -d ${INSTALL_DIR} \
    && rm wildfly-${VERSION}.zip \
    && chown -R serveradmin:serveradmin /opt \
    && chmod a+x ${WILDFLY_HOME}/bin/standalone.sh \
    && chmod -R a+rw ${INSTALL_DIR}
CMD mkdir -p {WILDFLY_HOME}/modules/system/layers/base/com/mysql/main
ADD mysql/module.xml {WILDFLY_HOME}/modules/system/layers/base/com/mysql/main/
ADD mysql/mysql-connector-java-8.0.12.jar {WILDFLY_HOME}/modules/system/layers/base/com/mysql/main/
USER serveradmin
ENTRYPOINT ${WILDFLY_HOME}/bin/standalone.sh  -c=standalone-full.xml -b=0.0.0.0
EXPOSE 8080 9990