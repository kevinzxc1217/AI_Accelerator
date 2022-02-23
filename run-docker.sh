#!/bin/bash

set -e

cd $(dirname "$0")

source env_setup.sh

cp env_setup.sh Docker/env_setup.sh

# build docker image
tag="playlab-$COURSE"
echo docker images -q $tag > /dev/null 2>&1
if [[ "$(docker images -q $tag)" == "" ]]; then
    docker build --build-arg UID=$(id -u) --build-arg GID=$(id -g) --build-arg NAME="$(id -un)" -t $tag ./Docker
fi

rm Docker/env_setup.sh

# $OSTYPE
#   windows => msys
#   mac => darwin

# run a docker container


if [[ $OSTYPE == "msys" ]]; then
    winpty docker run --privileged -v "/$PWD"/projects:/workspace/projects --name playlab-$COURSE -it --rm $tag bash
else
    docker run --privileged -v "$PWD"/projects:/workspace/projects --name playlab-$COURSE -it --rm $tag bash
fi
