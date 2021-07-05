#!/bin/bash

source "$PWD"/env_setup.sh

cp env_setup.sh "$PWD"/Docker/env_setup.sh

# build docker image
tag=$(echo "playlab-projects" | tr '[:upper:]' '[:lower:]')
echo tag: $tag
if [[ "$(docker images -q $tag > /dev/null 2>&1)" == "" ]]; then
    docker build -t $tag ./Docker
fi

# $OSTYPE
#   windows => msys
#   mac => darwin

# run a docker container
if [ $OSTYPE == "msys" ]; then
    winpty docker run -v "$PWD"/projects:/projects -p 8080:8080 -it $tag bash
else
    docker run -v "$PWD"/projects:/projects -p 8080:8080 -it $tag bash
fi
