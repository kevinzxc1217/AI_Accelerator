#!/bin/bash

# setup environment variables
ROOT="$PWD"
source "$ROOT/env_setup.sh"

[ ! -d "./projects" ] && mkdir projects

LABs=$(echo $PROJECT | tr "," "\n")

for LAB in $LABs
do
    cd "$ROOT"

    # Check if the repo exists
    git ls-remote https://playlab.computing.ncku.edu.tw:4001/$COURSE/$LAB.git -q > /dev/null 2>&1
    if [ $? != 0 ]; then
        echo -e "The repo $LAB does not exist\n"
        continue
    fi

    # Clone specified repo
    cd projects
    [ ! -d "./$LAB" ] && git clone https://playlab.computing.ncku.edu.tw:4001/$COURSE/$LAB.git

    cd $LAB

    # Setup personal repo 
    git config user.name $GIT_NAME
    git config user.email $GIT_EMAIL

    # Remove original upstream repo
    check=$(git remote -v | grep "playlab	https://playlab.computing.ncku.edu.tw:4001")
    if [[ $check ]]; then
        git remote remove playlab
    fi
    git remote add playlab https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$LAB.git

    git remote -v

    # create new upstream playlab repo
    check=$(git ls-remote https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$LAB.git)
    if [[ $check =~ "fatal" ]]; 
    then
        git push --set-upstream https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$LAB.git master
    else
        git fetch playlab
        git push playlab master
    fi
done

#download chisel-tutorial
cd $ROOT/projects
if [[ ! -d chisel-tutorial ]]; then
  git clone https://github.com/ucb-bar/chisel-tutorial.git
fi

# Run the docker container
echo "docker folder: $ROOT"
cd "$ROOT"

if [ $RUN_FLASK == true ]; then
  docker-compose up --build
else
  bash run-docker.sh
fi

