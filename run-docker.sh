#!/bin/bash -x

source env_setup.sh

# build docker image
tag=$(echo "playlab-$PROJECT" | tr '[:upper:]' '[:lower:]')
echo tag: $tag
if [[ "$(docker images -q $tag 2>dev/null)" == "" ]]; then
  docker build -t $tag ./Docker
fi

#run a docker containe
docker run -p 8080:8080 -v $PWD/${PROJECT}:/$PROJECT -it $tag /bin/bash 

