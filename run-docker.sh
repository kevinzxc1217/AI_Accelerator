#!/bin/bash +x

source "$PWD"/env_setup.sh

cp env_setup.sh "$PWD"/Docker/env_setup.sh

# build docker image
tag="playlab-projects"
echo docker images -q $tag > /dev/null 2>&1
if [[ "$(docker images -q $tag)" == "" ]]; then
    docker build -t $tag ./Docker
fi

# $OSTYPE
#   windows => msys
#   mac => darwin

# run a docker container
if [ $OSTYPE == "msys" ]; then
    winpty docker run -v "$PWD"/projects:/workspace/projects -p 8080:8080 --name playlab-projects -it $tag bash
else
    docker run -v "$PWD"/projects:/workspace/projects --name playlab-projects -p 8080:8080 -it $tag bash
fi
