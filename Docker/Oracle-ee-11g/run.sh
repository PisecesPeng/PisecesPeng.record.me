#!/bin/bash
set -e

# image_name = pp-oracle
# container_name = pp-oracle
#
# account = system
# password = oracle
# port_mapping(local:container) = 1521:1521
# memory = 1024M

# build Dockerfile
docker build -t pp-oracle .

# docker run
docker run -d -p 1521:1521 -v /home/demo/data:/u01/app/oracle -e DBCA_TOTAL_MEMORY=1024 --restart=always --name pp-oracle pp-oracle
