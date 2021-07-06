#!/bin/bash

source /Docker/env_setup.sh

# Setup personal repo 
git config --global user.name $GIT_NAME
git config --global user.email $GIT_EMAIL

cp /Docker/ngrok /bin/

if [ $RUN_FLASK == true ]; then
    uwsgi /Docker/uWSGI.ini &
else
  echo "Welcome to use the Playlab working environment~"
  cd /projects || exit 1
  exec /bin/bash -l 
fi
