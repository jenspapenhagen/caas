FROM base/java

LABEL maintainer="Jens Papenhagen <jens.papenhagen@web.de>" description="Wildfly 15.0.1 Final with datasource to an extern Mariadb/MySQL"
# please change the password with https://makemeapassword.ligos.net/api/v1/alphanumeric/json?c=1&l=25 or your password police

# Wildfly Appserver
ENV INSTALL_DIR /opt
ENV WILDFLY_USER serveradmin
ENV WILDFLY_PASS password
ENV WILDFLY_VERSION 15.0.1.Final
ENV WILDFLY_HOME ${INSTALL_DIR}/wildfly-${WILDFLY_VERSION}
ENV WILDFLY_LOG_DIR ${WILDFLY_HOME}/standalone/log/
ENV WILDFLY_DEPLOYMENT_DIR ${WILDFLY_HOME}/standalone/deployments/
ENV WILDFLY_CONFIGURATION_DIR ${WILDFLY_HOME}/standalone/configuration
ENV JBOSS_CLI ${WILDFLY_HOME}/bin/jboss-cli.sh

# setting up the linux user
RUN useradd -b /opt -m -s /bin/sh -d ${INSTALL_DIR} ${WILDFLY_USER} && echo ${WILDFLY_USER}:${WILDFLY_PASS} | chpasswd

# download wildfly 
RUN curl -O https://download.jboss.org/wildfly/${WILDFLY_VERSION}/wildfly-${WILDFLY_VERSION}.zip \
    && unzip wildfly-${WILDFLY_VERSION}.zip -d ${INSTALL_DIR} \
    && rm wildfly-${WILDFLY_VERSION}.zip \
    && chown -R ${WILDFLY_USER}:${WILDFLY_PASS} /opt \
    && chmod a+x ${WILDFLY_HOME}/bin/standalone.sh \
    && chmod -R 777 ${INSTALL_DIR}

# adding the user to the wildfly
RUN ${WILDFLY_HOME}/bin/add-user.sh -u ${WILDFLY_USER} -p ${WILDFLY_PASS} --silent

#download mariadb connector
RUN mkdir ${WILDFLY_HOME}/modules/system/layers/base/org/mariadb/jdbc/main \
    && curl -O https://downloads.mariadb.com/Connectors/java/connector-java-2.3.0/mariadb-java-client-2.3.0.jar 

ADD mariadb/module.xml ${WILDFLY_HOME}/modules/system/layers/base/org/mariadb/jdbc/main/

#download mysql connector
RUN mkdir  ${WILDFLY_HOME}/modules/system/layers/base/com/mysql/main \
    && curl -O https://bintray.com/bintray/jcenter/download_file?file_path=mysql%2Fmysql-connector-java%2F8.0.14%2Fmysql-connector-java-8.0.14.jar

ADD mysql/module.xml ${WILDFLY_HOME}/modules/system/layers/base/com/mysql/main/

#add a gernaric
ADD standalone-full.xml ${WILDFLY_CONFIGURATION_DIR}/

ENTRYPOINT ${WILDFLY_HOME}/bin/standalone.sh  -c=standalone-full.xml -b=0.0.0.0 -bmanagement=0.0.0.0
EXPOSE 8080 9990