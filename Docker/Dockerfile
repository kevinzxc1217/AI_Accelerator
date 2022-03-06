FROM ubuntu:18.04

ADD . /Docker

RUN mkdir -p /workspace/projects

WORKDIR /workspace/projects

ARG UID=1000
ARG GID=1000
ARG NAME=base

RUN apt-get update -y 

RUN DEBIAN_FRONTEND=noninteractive apt-get install -y apt-utils python3.6 python3-pip vim git nano dos2unix wget curl sudo locales openjdk-8-jre-headless

#Install SBT
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add 2> /dev/null

RUN apt-get update -y 
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y openjdk-8-jdk sbt 
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

#Install verilator
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y perl autoconf g++ flex bison ccache libgoogle-perftools-dev numactl perl-doc libfl2 libfl-dev zlibc 
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y verilator gdb cgdb valgrind
RUN unset VERILATOR_ROOT

# Update pip3
RUN DEBIAN_FRONTEND=noninteractive pip3 install --upgrade pip

# Change time zone
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends tzdata
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

ENV MILL_VERSION 0.10.0-M5

RUN curl -L https://github.com/com-lihaoyi/mill/releases/download/0.10.0-M5/0.10.0-M5 > /usr/local/bin/mill && chmod +x /usr/local/bin/mill

# Add qeumu environment
ENV QEMU_PACKAGES build-essential \
        ccache \
        chrpath \
        clang \
        cpio \
        diffstat \
        gawk \
        gettext \
        git \
        git-core \
        glusterfs-common \
        libaio-dev \
        libattr1-dev \
        libbrlapi-dev \
        libbz2-dev \
        libcacard-dev \
        libcap-ng-dev \
        libcurl4-gnutls-dev \
        libdrm-dev \
        libepoxy-dev \
        libfdt-dev \
        libgbm-dev \
        libibverbs-dev \
        libiscsi-dev \
        libjemalloc-dev \
        libjpeg-turbo8-dev \
        liblzo2-dev \
        libncursesw5-dev \
        libnfs-dev \
        libnss3-dev \
        libnuma-dev \
        libpixman-1-dev \
        librados-dev \
        librbd-dev \
        librdmacm-dev \
        libsasl2-dev \
        libseccomp-dev \
        libsnappy-dev \
        libspice-protocol-dev \
        libspice-server-dev \
        libssh-dev \
        libssl-dev \
        libusb-1.0-0-dev \
        libusbredirhost-dev \
        libvdeplug-dev \
        libvte-2.91-dev \
        libzstd-dev \
        locales \
        make \
        ninja-build \
        python3-yaml \
        python3-sphinx \
        python3-sphinx-rtd-theme \
        samba \
        sparse \
        texinfo \
        xfslibs-dev \
        libsdl2-dev 

RUN DEBIAN_FRONTEND=noninteractive apt-get -y install $QEMU_PACKAGES

# Add toolchain search path
ENV PATH=$PATH:/workspace/projects/riscv32-tools/riscv32-embecosm-ubuntu1804-gcc11.2.0/bin
ENV PATH=$PATH:/workspace/projects/riscv32-tools/xpack-riscv-none-embed-gcc-10.2.0-1.1/bin

# Add qemu command search path
ENV PATH=$PATH:/workspace/projects/qemu/build:$PATH

# Convert .sh files from CRLF to LF
RUN dos2unix -ic /Docker/start.sh | xargs dos2unix
RUN dos2unix -ic /Docker/env_setup.sh | xargs dos2unix

RUN cp /Docker/start.sh /usr/local/bin/ && chmod 755 /usr/local/bin/start.sh
USER $NAME
RUN echo eval /usr/local/bin/start.sh > ~/.bashrc 
RUN echo "export PS1=\"\[\e[0;31m\]\u@\[\e[m\e[0;34m\]\h\[\e[m \e[0;32m\] \w[\!]\$\[\e[m\]  \"" >> ~/.bashrc

CMD ["/bin/bash"]
