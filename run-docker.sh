#!/bin/bash +x

source "$PWD"/env_setup.sh

cp env_setup.sh "$PWD"/Docker/env_setup.sh

# build docker image
tag="playlab-$COURSE"
echo docker images -q $tag > /dev/null 2>&1
if [[ "$(docker images -q $tag)" == "" ]]; then
    docker build --build-arg UID=$(id -u) --build-arg GID=$(id -g) --build-arg NAME=$(id -un) -t $tag ./Docker
fi

# $OSTYPE
#   windows => msys
#   mac => darwin

# run a docker container

PORTs=$(echo $PORT_MAPPING | tr "," "\n")
PORT_OPTION=""

for PORT in $PORTs
do
    PORT_OPTION="$PORT_OPTION -p $PORT"
done

if [ $OSTYPE == "msys" ]; then
    winpty docker run --privileged -v "/$PWD"/projects:/workspace/projects -p $JUPYTER_PORT:10000 $PORT_OPTION --name playlab-$COURSE -it --rm $tag bash
else
    docker run --privileged -v "$PWD"/projects:/workspace/projects -p $JUPYTER_PORT:10000 $PORT_OPTION --name playlab-$COURSE -it --rm $tag bash
fi
