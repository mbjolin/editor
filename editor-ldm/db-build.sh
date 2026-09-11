#!/bin/bash

rm -rf docker/temp
mkdir -p docker/temp
cp -r src/* docker/temp/
cd docker
docker build -f Dockerfile -t mbjolin.ca:5000/editor-ldm:latest .