#!/bin/bash

welcome_message(){
    cat << EOF
  
Welcome to use the Playlab working environment

This docker environment is set up for the AIAS course
All projects are in the /workspace/projects directory

EOF
}

source /Docker/env_setup.sh

# setup personal repo 
git config --global user.name $GIT_NAME
git config --global user.email $GIT_EMAIL

#build verilator
#cd /workspace/projects/verilator
#autoconf
#./configure
#make -j; make install

sudo cp /Docker/ngrok /bin/
sudo chmod 755 /bin/ngrok

if [ $RUN_FLASK == true ]; then
    uwsgi -d /tmp/uWSGI.log /Docker/uWSGI.ini
else
    welcome_message
fi
