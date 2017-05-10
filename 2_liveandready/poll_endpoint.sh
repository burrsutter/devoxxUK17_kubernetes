#!/bin/bash
while true
do 
  curl --connect-timeout 1 -s 'http://liveandready-live.192.168.99.102.nip.io/'  
  sleep 1;
done
