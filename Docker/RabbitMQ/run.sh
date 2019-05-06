#!/bin/bash
set -e

# image_name = pp-rabbitmq
# container_name = pp-rabbitmq
#
# user = admin
# password = admin
#
# port_mapping(local:container) = 5672:5672
# port_mapping(local:container) = 15672:15672

# build Dockerfile
docker build -t pp-rabbitmq .

# docker run
# --hostname : RabbitMQ需要根据所谓的"节点名称"存储数据,默认为主机名.
docker run -d --hostname pp-rabbit -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 5672:5672 -p 15672:15672 -p 4369:4369 -p 25672:25672 --restart=always --name pp-rabbitmq pp-rabbitmq
