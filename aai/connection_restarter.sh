#!/bin/bash 
#0 * * * *
STATUS=$(curl -s -o /dev/null -w "%{http_code}" https://progresscfl.serveo.net)
if (( $STATUS > 499 )); then
    killall autossh
    /bin/sh ./create_connection.sh 
fi
