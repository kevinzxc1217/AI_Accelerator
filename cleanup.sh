#!/bin/bash +x

source ./env_setup.sh

# remove docker container
tag="playlab-$COURSE"
if [[ "$(docker ps -a -q  --filter ancestor=$tag)" != "" ]]; then
    docker rm $(docker ps -a -q  --filter ancestor=$tag) -f 
fi

# remove docker image
tag="playlab-$COURSE"
if [[ "$(docker images -q $tag)" != "" ]]; then
    docker rmi $tag
fi
