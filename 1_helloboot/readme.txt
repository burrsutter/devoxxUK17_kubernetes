http://start.spring.io

group:
com.whatever
artifact:
helloboot
Dependecies: Web

Generate and unzip

mvn clean compile package

Add MyRESTController.java

package com.whatever;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class MyRESTController {

    @RequestMapping("/")
    public String index() {
        return "Hello from Spring Boot! " + new java.util.Date();
    }

}

mvn clean compile package


java -jar target/helloboot-0.0.1-SNAPSHOT.jar

Browser: 
http://localhost:8080

Ctrl-C to stop

to run it on OpenShift (minishift)

mvn io.fabric8:fabric8-maven-plugin:3.3.5:setup

oc new-project bootie1

mvn fabric8:deploy

hit the openshift console to see the assigned route

kubectl get all

kubectl describe po/helloboot-1-42xt2

kubectl get po/helloboot-1-42xt2 -o yaml


