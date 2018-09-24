#!/bin/bash

# Make sure only root can run our script
if [ "$(id -u)" != "0" ]; then
   echo "This script must be run as root" 1>&2
   exit 1
fi

#create user 
adduser dbuser

#adding password for this user
echo dbuser:dbuser | chpasswd

#add user to sudo group
usermod -aG wheel dbuser

#switch to this user
su - dbuser

mysql -u dbuser -pdbuser caas < caas.sql