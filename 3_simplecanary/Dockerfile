FROM fabric8/java-jboss-openjdk8-jdk:1.2.3

ENV JAVA_APP_JAR myverticle-1.0-SNAPSHOT-fat.jar
ENV AB_ENABLED off
ENV AB_JOLOKIA_AUTH_OPENSHIFT true
ENV JAVA_OPTIONS -Xmx128m 

EXPOSE 8080

RUN chmod -R 777 /deployments/
ADD target/myverticle-1.0-SNAPSHOT-fat.jar /deployments/