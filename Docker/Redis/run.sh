#!/bin/bash
set -e

# image_name = pp-redis
# container_name = pp-redis
#
# password = qwerasdf
# port_mapping(local:container) = 6379:6379

# build Dockerfile
docker build -t pp-redis .

# docker run
docker run -d -p 6379:6379 --restart=always --name pp-redis pp-redis redis-server /etc/redis/redis.conf --requirepass "qwerasdf"
