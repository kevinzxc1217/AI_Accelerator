#!/bin/bash

# setup environment variables
ROOT="$PWD"
source "$ROOT/env_setup.sh"

[ ! -d "./projects" ] && mkdir "projects"

LABs=$(echo $PROJECT | tr "," "\n")

for LAB in $LABs
do
    cd "$ROOT"

    # check if the repo exists
    git ls-remote https://playlab.computing.ncku.edu.tw:4001/$COURSE_GITLAB/$LAB.git -q > /dev/null 2>&1
    if [ $? != 0 ]; then
        echo -e "The project repo : $LAB does not exist\n"
        continue
    fi

    # clone specified repo
    cd projects
    [ ! -d "./$LAB" ] && git clone https://playlab.computing.ncku.edu.tw:4001/$COURSE_GITLAB/$LAB.git

    cd $LAB

    # setup personal repo 
    git config user.name $GIT_NAME
    git config user.email $GIT_EMAIL

    # remove original upstream repo
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
        branch_name=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p')
        git push --set-upstream https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$LAB.git $branch_name
    else
        git fetch playlab
        git push playlab $branch_name
    fi
done

[ ! -d "$ROOT/www" ] && mkdir "$ROOT/www"

FLASK_LABs=$(echo $FLASK_PROJECT | tr "," "\n")

for FLASK_LAB in $FLASK_LABs
do
    cd "$ROOT/www"

    # check if the repo exists
    git ls-remote https://playlab.computing.ncku.edu.tw:4001/$COURSE_GITLAB/$FLASK_LAB.git -q > /dev/null 2>&1
    if [ $? == 0 ]; then
        [ ! -d "./$FLASK_LAB" ] && git clone https://playlab.computing.ncku.edu.tw:4001/$COURSE_GITLAB/$FLASK_LAB.git

        cd "$FLASK_LAB"

        # setup personal repo
        git config user.name $GIT_NAME
        git config user.email $GIT_EMAIL

        # remove original upstream repo
        check=$(git remote -v | grep "playlab	https://playlab.computing.ncku.edu.tw:4001")
        if [[ $check ]]; then
            git remote remove playlab
        fi
        git remote add playlab https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$FLASK_LAB.git

        git remote -v

        # create new upstream playlab repo
        check=$(git ls-remote https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$FLASK_LAB.git)
        if [[ $check =~ "fatal" ]]; 
        then
            branch_name=$(git branch | sed -n -e 's/^\* \(.*\)/\1/p')
            git push --set-upstream https://playlab.computing.ncku.edu.tw:4001/$GITLAB_LOGIN/$FLASK_LAB.git $branch_name
        else
            git fetch playlab
            git push playlab $branch_name
        fi
    else
        echo -e "The flask project repo : $FLASK_LAB does not exist\n"
    fi
done

# run the docker container
echo "docker folder: $ROOT"
cd "$ROOT"
cp env_setup.sh "$ROOT"/Docker/env_setup.sh

if [ $RUN_FLASK == true ]; then
    cd Docker
    cp ../env_setup.sh .env
    docker-compose build --no-cache
    docker-compose up -d

    if [ $OSTYPE == "msys" ]; then
        winpty docker exec -it "playlab-$COURSE" bash
    else
        docker exec -it "playlab-$COURSE" bash
    fi
else
    bash run-docker.sh
fi
