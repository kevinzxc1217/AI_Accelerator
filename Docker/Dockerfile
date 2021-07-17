FROM ubuntu:18.04

ADD . /Docker

WORKDIR /workspace/projects

ENV HOME /root
ENV DEBIAN_FRONTEND noninteractive
ARG UID=1000
ARG GID=1000

RUN mkdir /workspace/www

# Install python 3.6 and other softwares
RUN apt-get update -y 
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y apt-utils python3.6 python3-pip vim git nano dos2unix

# Update pip3
RUN pip3 install --upgrade pip

# Install required python modules
RUN pip3 install -r /Docker/requirements.txt

# Change time zone
RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

RUN groupadd -g $GID -o user

RUN useradd -u $UID -m -g user -G plugdev user && \
	echo 'user ALL = NOPASSWD: ALL' > /etc/sudoers.d/user && \
	chmod 0440 /etc/sudoers.d/user

RUN chown -R user:user /home/user

# Convert .sh files from CRLF to LF
RUN dos2unix -ic /Docker/start.sh | xargs dos2unix
RUN dos2unix -ic /Docker/env_setup.sh | xargs dos2unix

RUN cp /Docker/start.sh /usr/local/bin/ && chmod 755 /usr/local/bin/start.sh

ENTRYPOINT ["/usr/local/bin/start.sh"]
CMD ["/bin/bash"]
USER user
VOLUME [WORKDIR]


