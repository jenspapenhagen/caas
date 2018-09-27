FROM base/java

LABEL maintainer="Jens Papenhagen <jens.papenhagen@web.de>" description="Wildfly 14.0.1Final with datasource to an extern Mariadb/MySQL"

# Wildfly Appserver
ENV INSTALL_DIR /opt
ENV WILDFLY_USER serveradmin
ENV WILDFLY_PASS serveradmin
ENV WILDFLY_VERSION 14.0.1.Final
ENV WILDFLY_HOME ${INSTALL_DIR}/wildfly-${WILDFLY_VERSION}
ENV WILDFLY_LOG_DIR ${WILDFLY_HOME}/standalone/log/
ENV WILDFLY_DEPLOYMENT_DIR ${WILDFLY_HOME}/standalone/deployments/
ENV WILDFLY_CONFIGURATION_DIR ${WILDFLY_HOME}/standalone/configuration
ENV JBOSS_CLI ${WILDFLY_HOME}/bin/jboss-cli.sh

# Database
ENV DB_NAME caas
ENV DB_USER dbuser
ENV DB_PASS dbuser
ENV DB_URI db:3306

# Datasource
ENV DATASOURCE_NAME caas
ENV MYSQL_CONNECTOR 5.1.47

# setting up the linux user
RUN useradd -b /opt -m -s /bin/sh -d ${INSTALL_DIR} ${WILDFLY_USER} && echo ${WILDFLY_USER}:${WILDFLY_PASS} | chpasswd
USER serveradmin

# download wildfly 
RUN curl -O https://download.jboss.org/wildfly/${WILDFLY_VERSION}/wildfly-${WILDFLY_VERSION}.zip \
    && unzip wildfly-${WILDFLY_VERSION}.zip -d ${INSTALL_DIR} \
    && rm wildfly-${WILDFLY_VERSION}.zip \
    && chown -R ${WILDFLY_USER}:${WILDFLY_PASS} /opt \
    && chmod a+x ${WILDFLY_HOME}/bin/standalone.sh \
    && chmod -R a+rw ${INSTALL_DIR}

# adding the user to the wildfly
RUN ${WILDFLY_HOME}/bin/add-user.sh -u ${WILDFLY_USER} -p ${WILDFLY_PASS} --silent

# Configure Wildfly server and dry run it to adding the datasource
RUN echo "=> Starting WildFly server" && \
        bash -c '$WILDFLY_HOME/bin/standalone.sh &' && \
    echo "=> Waiting for the server to boot" && \
        bash -c 'until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do echo `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`; sleep 1; done' && \
    echo "=> Downloading MySQL driver/connector form Maven" && \
        curl --location --output /tmp/mysql-connector-java-${MYSQL_CONNECTOR}.jar --url http://search.maven.org/remotecontent?filepath=mysql/mysql-connector-java/${MYSQL_CONNECTOR}/mysql-connector-java-${MYSQL_CONNECTOR}.jar && \
    echo "=> Adding MySQL module" && \
        $JBOSS_CLI --connect --command="module add --name=com.mysql --resources=/tmp/mysql-connector-java-${MYSQL_CONNECTOR}.jar --dependencies=javax.api,javax.transaction.api" && \
    echo "=> Adding MySQL driver" && \
        $JBOSS_CLI --connect --command="/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql,driver-xa-datasource-class-name=com.mysql.jdbc.jdbc2.optional.MysqlXADataSource)" && \
    echo "=> Creating a new datasource" && \
        $JBOSS_CLI --connect --command="data-source add \
            --name=${DATASOURCE_NAME} \
            --jndi-name=java:/jdbc/datasources/${DATASOURCE_NAME} \
            --user-name=${DB_USER} \
            --password=${DB_PASS} \
            --driver-name=mysql \
            --connection-url=jdbc:mysql://${DB_URI}/${DB_NAME} \
            --use-ccm=false \
            --max-pool-size=25 \
            --blocking-timeout-wait-millis=5000 \
            --enabled=true" && \ 
    echo "=> Shutting down WildFly" && \
        $JBOSS_CLI --connect --command=":shutdown" && \ 
    echo "=> clean up"&& \
        rm -rf $WILDFLY_HOME/standalone/configuration/standalone_xml_history/ $WILDFLY_HOME/standalone/log/* && \ 
        rm -f /tmp/*.jar

ENTRYPOINT ${WILDFLY_HOME}/bin/standalone.sh  -c=standalone-full.xml -b=0.0.0.0 -bmanagement=0.0.0.0
EXPOSE 8080 9990