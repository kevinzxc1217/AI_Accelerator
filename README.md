# Docker Env for LABs

## 檔案架構
```
Dokcer Env for LABs
    ├── run.sh
    ├── docker-compose.yml
    ├── Docker
    │   ├── Dockerfile
    │   ├── requirements.txt
    │   ├── uWSGI.ini
    │   ├── nginx.conf
    │   ├── ngrok
    │   └── start.sh
    └── ProjectFiles
        └── server.py
```

## Shell Script `run.sh`
- 依需求修改 repo 的連結（第 1 行）及 repo 的名稱（第 2、3 行）
- script 自動將 repo 內的檔案移動至 `/ProjectFiles`，並刪除 `.git` 資料夾，最後建立 container


## Container : playlab-project
### Container 內外檔案架構對應
- `Docker` 資料夾複製到 container 內的 `/Docker`
- `ProjectFiles` 資料夾掛載到 container 內的 `/ProjectFiles`

### Flask Server
- `__main__` 檔案需命名為 `server.py`，並命名 Flask 物件為 `app`
    ```python
    # server.py

    app = Flask(__name__)

    if __name__ == '__main__':
        app.run()
    ```
- uWSGI Server 通過 `8080` port 轉發 nginx 與 flask server 連線
- 若 Lab 本身沒有要使用 flask server，可毋須移除 uWSGI 而不會發生啟動錯誤

### Shell Script `start.sh`
- 在 container 啟動後執行
- 將 `/Docker/ngrok` 設為環境變數 `$ngrok`
- 預設目錄為 `/Docker`


## Container : nginx
- 掛載外部設定檔 `/Docker/nginx.conf` 至 Container 內部 `/etc/nginx/conf.d/default.conf`
- 透過宿主機 http://localhost:8080 即可連線至 `/ProjectFiles` 內的 Flask Server
