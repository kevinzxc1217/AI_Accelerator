#!/bin/bash

welcome_message(){
  cat << EOF
  
Welcome to use the Playlab working environment

All projects are in the /workspace/projects directory
The nginx web server root is in the /workspace/www diretory

EOF
}


source /Docker/env_setup.sh

# Setup personal repo 
git config --global user.name $GIT_NAME
git config --global user.email $GIT_EMAIL

#build verilator
#cd /workspace/projects/verilator
#autoconf
#./configure
#make -j; make install

cp /Docker/ngrok /bin/

if [ $RUN_FLASK == true ]; then
    uwsgi /Docker/uWSGI.ini &
else
   welcome_message
   cd /workspace/projects || exit 1
   exec /bin/bash -l
fi


