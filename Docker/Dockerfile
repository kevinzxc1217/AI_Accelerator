FROM ubuntu:20.04

WORKDIR /Docker

ADD . /Docker

# Install python 3.8
RUN apt-get update -y && \
    apt-get install -y python3.8 python3-pip

# Change time zone
RUN DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends tzdata
RUN TZ=Asia/Taipei \
    && ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone \
    && dpkg-reconfigure -f noninteractive tzdata

# Update pip3
RUN pip3 install --upgrade pip

# Install required python modules
RUN pip3 install -r requirements.txt

# Execute /Docker/start.sh
CMD ["bash", "start.sh"]
