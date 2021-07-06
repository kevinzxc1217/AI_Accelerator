# PlayLab Docker Base

## Host 檔案架構
```bash
PlayLab Docker Base
    ├── env_setup.sh            # environment variables
    ├── run.sh                  # environment setup script
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

# project parameters, must be consistent with gitlab URLs
COURSE="aica-spring-2020"
PROJECT="aica_lab4,aica_lab5"       # ALL projects without flask
RUN_FLASK=false                     # start docker env with nginx proxy or not
FLASK_PROJECT="lab6_line_server"    # CURRENT flask project
```

![](https://playlab.computing.ncku.edu.tw:3001/uploads/upload_8e5dedffe9babd64353f34197dd71719.png)


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
- 宿主機 `/www` 內的 flask repo 掛載於 `/workspace/www`
- 宿主機 `/projects` 掛載於 `/workspace/projects`
- 可在 bash 使用 `ngrok` 直接呼叫預裝載的 ngrok
- uWSGI 通過 `8080` port 轉發 flask server 至 nginx，並透過宿主機 `8080` port 連線


## 一般環境
- 在 [環境設定參數](#環境設定參數) 內設定 `RUN_FLASK=false`
- 宿主機 `/projects` 掛載於 `/workspace/projects`
- 可在 bash 使用 `ngrok` 直接呼叫預裝載的 ngrok
- 宿主機與 container 的 `8080` port 相互對應

