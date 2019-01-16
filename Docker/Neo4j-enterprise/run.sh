#!/bin/bash
set -e

# image_name = pp-neo4j
# container_name = pp-neo4j
#
# account = admin
# port_mapping(local:container) = 7474:7474 & 7687:7687

# build Dockerfile
docker build -t pp-oracle .

# docker run
docker run -d -p 7474:7474 -p 7687:7687 -v /home/demo/data:/data --restart=always --name pp-neo4j pp-neo4j
