#!/bin/bash

welcome_message(){
    cat << EOF
  
Welcome to use the Playlab working environment

This docker environment is set up for the AIAS course
All projects are in the /workspace/projects directory

EOF
}

source /Docker/env_setup.sh

# setup personal repo 
git config --global user.name $GIT_NAME
git config --global user.email $GIT_EMAIL
git config --global core.editor vim

sudo chown -R "$(id -un)":"$(id -un)" /workspace

if [[ ! -d /workspace/projects/chisel-tutorial ]]; then
    echo clone chisel-tutorial.......
    git clone https://github.com/ucb-bar/chisel-tutorial.git /workspace/projects/chisel-tutorial
fi
if [[ ! -d /workspace/projects/chisel-template-lite ]]; then
    echo clone chisel-template-lite....
    git clone https://github.com/edwardcwang/chisel-template-lite.git /workspace/projects/chisel-template-lite
fi
if [[ ! -d /workspace/projects/rv32emu ]]; then
    echo clone rv32emu....
    git clone https://github.com/sysprog21/rv32emu /workspace/projects/rv32emu
fi
if [[ ! -d /workspace/projects/qemu ]]; then
    echo clone qemu....
    git clone --depth=1 https://git.qemu.org/git/qemu.git /workspace/projects/qemu
fi

if [[ ! -d /workspace/projects/riscv32-tools ]]; then
    echo setup riscv32-tools .....
    mkdir -p /workspace/projects/riscv32-tools
    wget https://github.com/xpack-dev-tools/riscv-none-embed-gcc-xpack/releases/download/v10.2.0-1.1/xpack-riscv-none-embed-gcc-10.2.0-1.1-linux-x64.tar.gz -P /workspace/projects/riscv32-tools/
    wget --no-check-certificate https://buildbot.embecosm.com/job/riscv32-gcc-ubuntu1804-release/10/artifact/riscv32-embecosm-ubuntu1804-gcc11.2.0.tar.gz -P /workspace/projects/riscv32-tools/
    tar zxvf /workspace/projects/riscv32-tools/riscv32-embecosm-ubuntu1804-gcc11.2.0.tar.gz -C /workspace/projects/riscv32-tools/
    tar zxvf /workspace/projects/riscv32-tools/xpack-riscv-none-embed-gcc-10.2.0-1.1-linux-x64.tar.gz -C /workspace/projects/riscv32-tools/
    rm /workspace/projects/riscv32-tools/riscv32-embecosm-ubuntu1804-gcc11.2.0.tar.gz
    rm /workspace/projects/riscv32-tools/xpack-riscv-none-embed-gcc-10.2.0-1.1-linux-x64.tar.gz
fi

welcome_message
