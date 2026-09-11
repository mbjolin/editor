#!/bin/bash

#https://stackoverflow.com/questions/3510673/find-and-kill-a-process-in-one-line-using-bash-and-regex
kill $(ps aux | grep 'quarkus' | awk '{print $2}')
kill -9 $(ps aux | grep 'quarkus' | awk '{print $2}')

#https://docs.oracle.com/cd/E19253-01/806-4743/processmanagerusing-62/index.html