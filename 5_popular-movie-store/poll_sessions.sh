#!/bin/bash

while true
do 
  curl --connect-timeout 1 -s 'http://popular-movie-store-movies.192.168.99.102.nip.io/sessions' | jq '.hostName'
  echo '-------------------------------'
  sleep 1;
done
