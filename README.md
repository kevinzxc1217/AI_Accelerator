# PlayLab Docker Base

## Table of Contents
- [Host 檔案架構](#host-檔案架構)
- [Container 檔案架構](#container-檔案架構)
- [環境設定參數](#環境設定參數)
- [Python Module List](#python-module-list)
- [Flask 環境](#flask-環境)
- [一般環境](#一般環境)
- [Demo](#demo)
- [Reference](#reference)


## Host 檔案架構
```bash
PlayLab Docker Base
    ├── env_setup.sh            # environment variables
    ├── run.sh                  # environment setup script
    ├── run-docker.sh           # docker run script without nginx
    ├── Docker/
    │   ├── docker-compose.yml
    │   ├── Dockerfile
    │   ├── requirements.txt    # python module list
    │   ├── uWSGI.ini
    │   ├── nginx.conf
    │   ├── ngrok               # version 2.3.40
    │   └── start.sh            # container entrypoint
    ├── projects/               # projects repos without flask
    └── www/                    # flask project repo
```


## Container 檔案架構
```bash
workspace/
    ├── projects/       # ALL repos without flask
    └── www/            # CURRENT flask project
```


## 環境設定參數
```bash
# env_setup.sh

# personal settings
GIT_NAME=<your git name>
GIT_EMAIL=<your git email>
GITLAB_LOGIN=<your playlab gitlab login name>

# docker configuration
COURSE=base

# setup docker web service port mapping (format => host:container)
PORT_MAPPING=
NGINX_PORT=8080

# start docker env with / without uWSGI and nginx proxy
RUN_FLASK=true

# project parameters, must be consistent with gitlab URLs
COURSE_GITLAB="aica-spring-2020"

# normal project list
PROJECT="aica_lab4"

# flask project list
FLASK_PROJECT="lab6_line_server"

# mount to /workspace/www in container
CURRENT_FLASK_FOLDER=lab6_line_server
```

![](https://playlab.computing.ncku.edu.tw:3001/uploads/upload_8e5dedffe9babd64353f34197dd71719.png)


## Python Module List
```bash
# /Docker/requirements.txt

Flask==2.0.1
uWSGI==2.0.19
```

- 建議指定安裝版本，避免未來更新造成的相容性問題
- 查詢已安裝的套件清單 & 版本
    ```bash
    $ pip list
    ```


## Flask 環境
- 在 [環境設定參數](#環境設定參數) 內設定 `RUN_FLASK=true`
- `__main__` 檔案需命名為 app.py，並命名 Flask 物件為 `app`
    ```python
    # app.py

    from flask import Flask
    app = Flask(__name__)

    @app.route("/")
    def hello_world():
        return "<p>Hello, World!</p>"
    ```
- 宿主機 `./www` 內的 flask repo 掛載於 `/workspace/www`
- 宿主機 `./projects` 掛載於 `/workspace/projects`
- 可在 bash 使用 `ngrok` 直接呼叫預裝載的 ngrok
- Docker container 內 `/workspace/www/app.py` 可透過宿主機 `NGINX_PORT` port 連線存取
- 使用完畢後需自行關閉 container
    ```bash
    $ docker-compose down
    ````


## 一般環境
- 在 [環境設定參數](#環境設定參數) 內設定 `RUN_FLASK=false`
- 宿主機 `./projects` 掛載於 `/workspace/projects`
- 可在 bash 使用 `ngrok` 直接呼叫預裝載的 ngrok
- 透過 `PORT_MAPPING` 指定宿主機與 container 相互映射的 port，並可透過分隔符號 `,` 同時指定多組映射
- 關閉 bash window 後 container 將會立即關閉


## SQL Support
- 在 [/Docker/docker-compose.yml](https://playlab.computing.ncku.edu.tw:4001/CTPS/playlab-docker-base/blob/master/Docker/docker-compose.yml) 內加上對應的 service

### Postgres Example
- Docker Hub Reference : [postgres](https://hub.docker.com/_/postgres)

```yml
# /Docker/docker-compose.yml

version: "3"

services:
    # ... skip ...
    postgres:
        image: postgres:13.3-alpine
        volumes:
            - ./data/db:/var/lib/postgresql/data
        environment:
            - POSTGRES_DB=postgres-db
            - POSTGRES_USER=postgres-user
            - POSTGRES_PASSWORD=postgres-pwd
```

### MySQL Example
- Docker Hub Reference : [mariadb](https://hub.docker.com/_/mariadb)

```yml
# /Docker/docker-compose.yml

version: "3"

services:
    # ... skip ...
    mysql:
        image: mariadb
        restart: always
        volumes:
            - ./db:/var/lib/mysql
        environment:
            - MYSQL_ROOT_PASSWORD=adminpwd
            - MYSQL_PASSWORD=userpwd
            - MYSQL_DATABASE=dbname
            - MYSQL_USER=user
```


## Demo
- Non-flask mode：https://youtu.be/3V51_ke31Bg
- Flask mode：https://youtu.be/BqmUY_oP9H0


## Reference
- [【Flask教學系列】Flask 為甚麼需要 WSGI 與 Nginx](https://www.maxlist.xyz/2020/05/06/flask-wsgi-nginx/)
