oc new-project movie
Note: there is a reference to "movie" in the pom.xml 
as 
<openshift.project>movie</openshift.project>

Detailed instructions from Kamesh, the originator of this demo
https://github.com/kameshsampath/infinispan-demo-apps/wiki/Popular-Movie-Store

Note: these next two lines should now be optional based on src/main/fabric8/rb.yml 
These lines are vital for allowing KUBE_PING (Infinispan to see cluster members)

oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default -n $(oc project -q)

oc policy add-role-to-user view system:serviceaccount:$(oc project -q):popular-movie-store -n $(oc project -q)

note: not my real API Key
./mvnw -DapiKey=YtWlNDU0NTI1OWYwXNdhY3AiMTllMzMyYTVmZDuyY2U= clean fabric8:deploy 

Make a change to cart.html

./mvnw -Pcanary -DapiKey=YtWlNDU0NTI1OWYwXNdhY3AiMTllMzMyYTVmZDuyY2U= clean fabric8:deploy 

Then use the OpenShift console to change the mix of pods


