# personal settings
GIT_NAME="Wei-Fen Lin"
GIT_EMAIL=weifen.lin@gs.ncku.edu.tw
GITLAB_LOGIN=weifen

# docker configuration
COURSE=base

# setup docker web service port mapping (format => host:container)
PORT_MAPPING=
NGINX_PORT=8080
JUPYTER_PORT=10000

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
