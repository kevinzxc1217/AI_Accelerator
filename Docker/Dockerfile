FROM ubuntu:20.04

ADD . /Docker

WORKDIR /workspace

RUN mkdir /workspace/projects
RUN mkdir /workspace/www

# Install python 3.8
RUN apt-get update -y && \
    apt-get install -y apt-utils python3.8 python3-pip vim git nano dos2unix

# Change time zone
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends tzdata
RUN TZ=Asia/Taipei \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && dpkg-reconfigure -f noninteractive tzdata

# Update pip3
RUN pip3 install --upgrade pip

# Install required python modules
RUN pip3 install -r /Docker/requirements.txt

# Convert CRLF to LF
RUN dos2unix -ic /Docker/start.sh | xargs dos2unix
RUN dos2unix -ic /Docker/env_setup.sh | xargs dos2unix

RUN cp /Docker/start.sh /usr/local/bin/ && chmod 755 /usr/local/bin/start.sh 

ENTRYPOINT ["/usr/local/bin/start.sh"]
