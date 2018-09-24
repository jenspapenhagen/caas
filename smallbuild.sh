mvn clean install
docker build -t base/app .
docker remove localhost:5000/base/app
docker push localhost:5000/base/app
docker kill base/app
docker rm base/app

#need to get finale line soon
#docker run -d -p 8082:8080 -p 9992:9990 -u serveradmin:serveradmin --name caas -v /opt/wildfly/standalone/log:/opt/wildfly-14.0.1.Final/standalone/log -v /opt/wildfly/standalone/deployments:/opt/wildfly-14.0.1.Final/standalone/deployments -v /opt/wildfly/standalone/configuration:/opt/wildfly-14.0.1.Final/standalone/configuration localhost:5000/base/app /opt/jboss/wildfly/bin/standalone.sh -c standalone-full.xml -b 0.0.0.0 -bmanagement 0.0.0.0