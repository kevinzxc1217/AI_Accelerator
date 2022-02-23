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

sudo chown -R $(id -un):$(id -un) /workspace

if [[ ! -d /workspace/projects/chisel-tutorial ]]; then
    git clone https://github.com/ucb-bar/chisel-tutorial.git /workspace/projects/chisel-tutorial
fi
if [[ ! -d /workspace/projects/chisel-template-lite ]]; then
    git clone https://github.com/edwardcwang/chisel-template-lite.git /workspace/projects/chisel-template-lite
fi
if [[ ! -d /workspace/projects/rv32emulator ]]; then
    git clone https://github.com/sangwoojun/rv32emulator /workspace/projects/rv32emulator
fi
if [[ ! -d /workspace/projects/rv32emu ]]; then
    git clone https://github.com/sysprog21/rv32emu /workspace/projects/rv32emu
fi
if [[ ! -d /workspace/projects/qemu ]]; then
    git clone --depth=1 https://git.qemu.org/git/qemu.git /workspace/projects/qemu
fi

welcome_message
