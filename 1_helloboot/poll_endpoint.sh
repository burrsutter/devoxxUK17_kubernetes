#!/bin/bash
while true
do 
  curl --connect-timeout 1 -s 'http://helloboot-bootie1.192.168.99.102.nip.io/'
  sleep 1;
done
