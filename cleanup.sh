#!/bin/bash +x

source ./env_setup.sh

# shut down the services
cd Docker
docker-compose down
cd ..

# remove docker image
tag="playlab-$COURSE"
echo docker images -q $tag > /dev/null 2>&1
if [[ "$(docker images -q $tag)" != "" ]]; then
    docker rmi $tag
fi
