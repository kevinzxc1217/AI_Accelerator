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

sudo chmod -R 777 /workspace

# Add toolchain search path  
export

# Environment build up
cd /workspace/projects/rv32emulator
make clean
make

cd /workspace/projects/rv32emu
make
make check
make arch-test RISCV_DEVICE=I

welcome_message
