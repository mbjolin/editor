#!/bin/bash

docker run -d -p 5432:5432 --restart unless-stopped --env DATASET=base --name editor-ldm mbjolin.ca:5000/editor-ldm:latest
sleep 3
printf $(docker logs editor-ldm)