#!/bin/bash -x

# setup environment variables
source env_setup.sh
ROOT=$PWD

# Clone specified repo
[ ! -d "./$PROJECT" ] && git clone https://playlab.computing.ncku.edu.tw:4001/aica-spring-2020/$PROJECT


# Setup personal repo 
git config --global user.name $GIT_NAME
git config --global user.email $GIT_EMAIL

# create new upstream playlab repo
cd $PROJECT
check=$(git remote -v | grep "playlab	https://playlab.computing.ncku.edu.tw:4001")
if [[ $check ]]; then
  git remote remove playlab
fi
git remote add playlab https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$PROJECT

git remote -v

check=$(git ls-remote https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$PROJECT.git)
if [[ $check =~ "fatal" ]]; 
then
  git push --set-upstream https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$PROJECT.git master
else
  git fetch playlab
  git push playlab master
fi

# Run the docker container
echo "root folder: $ROOT"
cd $ROOT
cp env_setup.sh .env

bash run-docker.sh

#docker-compose up --build

