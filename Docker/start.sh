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
    wget --no-check-certificate https://osp.computing.ncku.edu.tw:8001/share.cgi?ssid=e7b5fa10dc1f4e87aa89082ce4da6bb4&fid=e7b5fa10dc1f4e87aa89082ce4da6bb4&path=%2F&filename=riscv32-elf-ubuntu-20.04-nightly-2022.03.09-nightly.tar.gz&openfolder=forcedownload&ep= -O riscv32-elf.tar.gz -P /workspace/projects/riscv32-tools/
    tar zxvf /workspace/projects/riscv32-tools/riscv32-elf.tar.gz -C /workspace/projects/riscv32-tools/
    rm /workspace/projects/riscv32-tools/riscv32-elf.tar.gz
fi

welcome_message
echo aias fall 2022 workspace is created.