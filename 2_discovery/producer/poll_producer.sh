#!/bin/bash
while true
do 
  curl --connect-timeout 1 -s 'http://producer-demo.192.168.99.104.nip.io/'
  sleep 1;
done
