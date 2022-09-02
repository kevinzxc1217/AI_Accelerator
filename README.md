# PlayLab Docker Base

## Table of Contents
- [PlayLab Docker Base](#playlab-docker-base)
  - [Table of Contents](#table-of-contents)
  - [Host 檔案架構](#host-檔案架構)
  - [Container 檔案架構](#container-檔案架構)
  - [環境設定參數](#環境設定參數)
  - [Python Module List](#python-module-list)
  - [一般環境](#一般環境)


## Host 檔案架構
```bash
PlayLab Docker Base
    ├── env_setup.sh            # environment variables
    ├── run.sh                  # environment setup script
    ├── run-docker.sh           # docker run script 
    └── Docker/
        ├── Dockerfile
        ├── requirements.txt    # python module list
        └── start.sh            # container entrypoint
   
```


## Container 檔案架構
```bash
workspace/
    └── projects/       # ALL repos 
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

```

![](https://playlab.computing.ncku.edu.tw:3001/uploads/upload_8e5dedffe9babd64353f34197dd71719.png)


## Python Module List
```bash
# /Docker/requirements.txt

```

- 建議指定安裝版本，避免未來更新造成的相容性問題
- 查詢已安裝的套件清單 & 版本
    ```bash
    $ pip list
    ```


## 一般環境
- 宿主機 `./projects` 掛載於 `/workspace/projects`
- 關閉 bash window 後 container 將會立即關閉
