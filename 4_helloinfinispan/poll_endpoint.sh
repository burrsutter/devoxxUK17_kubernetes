#!/bin/bash
while true
do 
  curl --connect-timeout 1 -s 'http://myapp-nspace.192.168.99.102.nip.io/curlstuff'   
  sleep 1;
done
