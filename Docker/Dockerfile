FROM ubuntu:20.04

ADD . /Docker

WORKDIR /workspace

RUN mkdir /workspace/projects
RUN mkdir /workspace/www

# Install python 3.8
RUN apt-get update -y && \
    apt-get install  --fix-missing -y apt-utils python3.8 python3-pip vim git nano dos2unix \
      wget curl  

# Change time zone
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends tzdata
RUN TZ=Asia/Taipei \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && dpkg-reconfigure -f noninteractive tzdata

#Install SBT
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add

RUN apt-get update -y && \
    apt-get install -y openjdk-8-jdk sbt 
RUN JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

#Install verilator
RUN apt-get install -y perl autoconf g++ flex bison ccache libgoogle-perftools-dev numactl perl-doc libfl2 libfl-dev zlibc 
RUN apt-get install -y verilator
RUN unset VERILATOR_ROOT # For bash, unsetenv for csh

# Update pip3
RUN pip3 install --upgrade pip

# Install required python modules
RUN pip3 install -r /Docker/requirements.txt

# Convert CRLF to LF
RUN dos2unix -ic /Docker/start.sh | xargs dos2unix
RUN dos2unix -ic /Docker/env_setup.sh | xargs dos2unix

RUN cp /Docker/start.sh /usr/local/bin/ && chmod 755 /usr/local/bin/start.sh 

ENTRYPOINT ["/usr/local/bin/start.sh"]
