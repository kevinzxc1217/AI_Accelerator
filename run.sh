#!/bin/bash +x


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

#download chisel-tutorial
cd $ROOT/projects
if [[ ! -d chisel-tutorial ]]; then
  git clone https://github.com/ucb-bar/chisel-tutorial.git
fi
if [[ ! -d verilator ]]; then
  git clone https://github.com/verilator/verilator
  cd verilator 
  git pull 
fi
if [[ ! -d chisel-template-lite ]]; then
  git clone https://github.com/edwardcwang/chisel-template-lite.git
fi
if [[ ! -d rv32emulator ]]; then
  git clone https://github.com/sangwoojun/rv32emulator
fi
if [[ ! -d qemu ]]; then
  git clone https://git.qemu.org/git/qemu.git
fi
if [[ ! -d rv32emu ]]; then
  git clone https://github.com/sysprog21/rv32emu
fi
if [[ ! -d riscv32-tools ]]; then
  mkdir -p /workspace/projects/riscv32-tools
  cd riscv32-tools
  wget https://buildbot.embecosm.com/job/riscv32-gcc-ubuntu1804-release/10/artifact/riscv32-embecosm-ubuntu1804-gcc11.2.0.tar.gz
  wget https://github.com/xpack-dev-tools/riscv-none-embed-gcc-xpack/releases/download/v10.2.0-1.1/xpack-riscv-none-embed-gcc-10.2.0-1.1-linux-x64.tar.gz
  tar zxvf riscv32-embecosm-ubuntu1804-gcc11.2.0.tar.gz
  tar zxvf xpack-riscv-none-embed-gcc-10.2.0-1.1-linux-x64.tar.gz
fi


# run the docker container
echo "docker folder: $ROOT"
cd "$ROOT"
cp env_setup.sh Docker/ 

bash run-docker.sh

