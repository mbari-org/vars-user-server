#!/usr/bin/env bash

echo "--- Building vars-user-server (reminder: run docker login first!!)"

sbt pack && \
    docker build -t mbari/vars-user-server . && \
    docker push mbari/vars-user-server
