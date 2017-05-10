oc new-project liveone

mvn clean fabric8:deploy -DskipTests

chmod +x poll_endpoint.sh

./poll_endpoint.sh

http://liveandready-live.192.168.99.102.nip.io/preparetodie
Note: "live" is the name of the project/namespace

This will mark a pod as dead, just a boolean flag.
The liveness probe returns an error and Kubernetes/OpenShift
restarts it.  Because it is restarted, the flag is reset.

kubetail liveandready 

to see the logs of N pods together

https://github.com/johanhaleby/kubetail

More information on liveness and readiness
https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-probes/#defining-a-liveness-http-request








