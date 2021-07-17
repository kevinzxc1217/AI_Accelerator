# personal settings
GIT_NAME="Wei-Fen Lin"
GIT_EMAIL=weifen.lin@gs.ncku.edu.tw
GITLAB_LOGIN=weifen

# docker configuration
COURSE="base"
PORT_MAPPING="3000:3000"                        # host:container
NGINX_PORT="8080"                               # host

# project parameters, must be consistent with gitlab URLs
RUN_FLASK=false                                 # start docker env with / without uWSGI and nginx proxy
COURSE_GITLAB="aica-spring-2020"
PROJECT="aica_lab4"                   # projects without flask
FLASK_PROJECT="lab6_line_server"                # flask projects
CURRENT_FLASK_FOLDER="www/lab6_line_server"     # mount to /workspace/www in container
