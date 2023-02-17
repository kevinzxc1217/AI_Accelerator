FROM ubuntu:20.04

ADD . /Docker

RUN mkdir -p /workspace/projects

WORKDIR /workspace/projects

ARG UID=1000
ARG GID=1000
ARG NAME=base

ARG INSTALLATION_TOOLS apt-utils \
    sudo \
    curl \
    wget \
    software-properties-common

ARG TOOL_PACKAGES dos2unix \
    git \
    nano \
    vim \
    tree \
    locales \
    python3-pip \
    make \
    build-essential \
    valgrind \
    gdb \
    verilator \
    qemu-system-riscv32

# install essential tools and packages
RUN apt-get update -qq && \
    apt-get install ${INSTALLATION_TOOLS} -yqq && \
    add-apt-repository ppa:git-core/ppa && \
    apt-get update -qq && \
    apt-get upgrade -yqq && \
    apt-get install ${TOOL_PACKAGES} -yqq

# install sbt
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add 2> /dev/null

# install mill
ENV MILL_VERSION 0.10.0-M5
RUN wget -q "https://github.com/com-lihaoyi/mill/releases/download/0.10.0-M5/0.10.0-M5" -O /usr/local/bin/mill && chmod +x /usr/local/bin/mill

# install JAVA and set $JAVA_HOME
RUN apt-get update -qq
RUN apt-get install openjdk-8-jdk sbt -yqq
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Change time zone
RUN apt-get install -yqq --no-install-recommends tzdata
RUN TZ=Asia/Taipei \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && dpkg-reconfigure -f noninteractive tzdata

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

RUN groupadd -g $GID -o $NAME

RUN useradd -u $UID -m -g $NAME -G plugdev $NAME && \ 
	echo "$NAME ALL = NOPASSWD: ALL" > /etc/sudoers.d/user && \
	chmod 0440 /etc/sudoers.d/user

RUN chown -R $NAME:$NAME /home/$NAME
RUN chown -R $NAME:$NAME /Docker

# Install required python modules
RUN pip3 install -r /Docker/requirements.txt

# set install path of tools about risc-v
ENV RISCV=/opt/riscv

# install sifive elf2hex (Verilog/Chisel friendly hex file generator)
ARG SIFIVE_ELF2HEX_URL=https://github.com/sifive/elf2hex/releases/download/v1.0.1/elf2hex-1.0.1.tar.gz
RUN mkdir -p ${RISCV} && cd ${RISCV} && \
    wget -q ${SIFIVE_ELF2HEX_URL} && \
    tar -xvzpf elf2hex-1.0.1.tar.gz >> /dev/null && \
    cd elf2hex-1.0.1 && \
    ./configure --target=riscv64-unknown-elf && \
    make && \
    make install && \
    cd .. && rm -rf elf2hex-1.0.1.tar.gz elf2hex-1.0.1

# install RISC-V GNU Toolchain (x86_64 or Arm64 according to ARCH)
ARG RISCV_GNU_TOOLCHAIN_URL_X86_64="https://playlab.computing.ncku.edu.tw/downloads/riscv-gnu-toolchain/riscv64-elf-Linux-x86_64-65056bd.tar.gz"
ARG RISCV_GNU_TOOLCHAIN_URL_ARM64="https://playlab.computing.ncku.edu.tw/downloads/riscv-gnu-toolchain/riscv64-elf-Linux-aarch64-65056bd.tar.gz"
ARG ARCH
RUN cd $RISCV && \
    mkdir riscv-gnu-toolchain && \
    if [ "$ARCH" = "x86_64" ]; then \
        wget -q $RISCV_GNU_TOOLCHAIN_URL_X86_64 -O riscv-gnu-toolchain.tar.gz; \
    else \
        wget -q $RISCV_GNU_TOOLCHAIN_URL_ARM64 -O riscv-gnu-toolchain.tar.gz; \
    fi && \
    tar zxvf riscv-gnu-toolchain.tar.gz -C riscv-gnu-toolchain --strip-components 1 >> /dev/null && \
    rm -rf riscv-gnu-toolchain.tar.gz
# add RISC-V GNU Toolchain to system search path
ENV PATH=$PATH:$RISCV/riscv-gnu-toolchain/bin

# Convert .sh files from CRLF to LF
RUN dos2unix -ic /Docker/start.sh | xargs dos2unix
RUN dos2unix -ic /Docker/env_setup.sh | xargs dos2unix

RUN cp /Docker/start.sh /usr/local/bin/ && chmod 755 /usr/local/bin/start.sh
USER $NAME
RUN echo eval /usr/local/bin/start.sh > ~/.bashrc 
RUN echo "export PS1=\"\[\e[0;31m\]\u@\[\e[m\e[0;34m\]\h\[\e[m \e[0;32m\] \w[\!]\$\[\e[m\]  \"" >> ~/.bashrc

# run bash
CMD ["/bin/bash"]
