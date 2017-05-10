Note: no fabric8, all manual "oc" but not yaml

mvn clean compile package

java -jar target/myverticle-1.0-SNAPSHOT-fat.jar

http://localhost:8080
http://localhost:8080/api/health

ctrl-c

oc new-project simple

Deploy V1

oc new-build --binary --name=aloha
oc start-build aloha --from-dir=. --follow
oc new-app aloha
oc expose service aloha
oc get routes
curl the route
oc set probe dc/aloha --readiness --get-url=http://:8080/api/health

if you make a code change to V1, simply 
mvn clean compile package
oc start-build aloha --from-dir=. --follow

Deploy V2 (the canary)
make a code change
oc new-build --binary --name=aloha-canary

mvn clean compile package
oc start-build aloha-canary --from-dir=. --follow
oc new-app aloha-canary
oc set probe dc/aloha-canary --readiness --get-url=http://:8080/api/health

Now here is the magic, apply the same label to both V1 and V2 
oc patch dc/aloha -p '{"spec":{"template":{"metadata":{"labels":{"svc":"canary-aloha"}}}}}'
oc patch dc/aloha-canary -p '{"spec":{"template":{"metadata":{"labels":{"svc":"canary-aloha"}}}}}'

This step causes redeployment of the pods
Then update the V1's service for a selector on that label
oc patch svc/aloha -p '{"spec":{"selector":{"svc":"canary-aloha","app": null, "deploymentconfig": null}, "sessionAffinity":"ClientIP"}}'

Change the percentages - out-of-the-box round-robin load-balancing
oc scale --replicas=4 dc/aloha
oc scale --replicas=0 dc/aloha-canary







