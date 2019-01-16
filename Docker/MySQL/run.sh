#!/bin/bash
set -e

# image_name = pp-mysql
# container_name = pp-mysql
#
# account = root
# password = qwerasdf
# port_mapping(local:container) = 3306:3306


# build Dockerfile
docker build -t pp-mysql .

# docker run
docker run -d -e MYSQL_ROOT_PASSWORD=qwerasdf -p 3306:3306 --restart=always --name pp-mysql pp-mysql
