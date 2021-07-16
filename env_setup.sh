# personal settings
GIT_NAME=<your git name>
GIT_EMAIL=<your git email>
GITLAB_LOGIN=<your playlab gitlab login name>

# docker configuration
COURSE="aica"
PORT_MAPPING="3000:3000"                        # host:container
NGINX_PORT="8080"                               # host

# project parameters, must be consistent with gitlab URLs
RUN_FLASK=false                                 # start docker env with / without uWSGI and nginx proxy
COURSE_GITLAB="aica-spring-2020"
PROJECT="aica_lab4,aica_lab5"                   # projects without flask
FLASK_PROJECT="lab6_line_server"                # flask projects
CURRENT_FLASK_FOLDER="www/lab6_line_server"     # mount to /workspace/www in container
