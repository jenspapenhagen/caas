#!/bin/bash

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

#copy all files from the test dev deployment into /opt/
cp -rf /devdeploy/* /opt/wildfly/


#create user 
adduser serveradmin

#adding password for this user
echo serveradmin:serveradmin | chpasswd

#add user to sudo group
usermod -aG wheel serveradmin

#switch to this user
#su - serveradmin

#build the registry and run it all the day
docker run -d -p 5000:5000 --restart=always --name registry registry:2

#build the base java images form centOS and the jdk
cd java
docker build -t base/java .

cd ..
cd wildfly
docker build -t base/wildfly .

cd ..
cd app
docker build -t base/app/ .

#re-tag all the images for the registy
docker tag base/java localhost:5000/base/java
docker tag base/wildfly localhost:5000/base/wildfly
docker tag base/app localhost:5000/base/app

#push all images to the registy
docker push localhost:5000/base/java
docker push localhost:5000/base/wildfly
docker push localhost:5000/base/app

#remove/untag the old local images
docker image remove base/java
docker image remove base/wildfly
docker image remove base/app

#run the app image
docker run -d -p 8082:8080 -p 9992:9990 -u serveradmin:serveradmin --name caas -v /data/container/caas/log:/opt/wildfly-14.0.1.Final/standalone/log -v /data/container/caas/deployments:/opt/wildfly-14.0.1.Final/standalone/deployments -v /data/container/caas/configuration:/opt/wildfly-14.0.1.Final/standalone/configuration localhost:5000/base/app /opt/jboss/wildfly/bin/standalone.sh -c standalone-full.xml -b 0.0.0.0 -bmanagement 0.0.0.0