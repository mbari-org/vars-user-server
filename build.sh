#!/usr/bin/env bash

echo "--- Building vars-user-server (reminder: run docker login first!!)"

BUILD_DATE=`date -u +"%Y-%m-%dT%H:%M:%SZ"`
VCS_REF=`git tag | sort -V | tail -1`

sbt pack && \
    docker build --build-arg BUILD_DATE=$BUILD_DATE \
                 --build-arg VCS_REF=$VCS_REF \
                  -t mbari/vars-user-server:${VCS_REF} \
                  -t mbari/vars-user-server:latest . && \
    docker push mbari/vars-user-server

