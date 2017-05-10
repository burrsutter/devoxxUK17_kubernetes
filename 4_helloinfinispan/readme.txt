OpenShift:

to run it on OpenShift (minishift)

mvn io.fabric8:fabric8-maven-plugin:3.3.5:setup

oc new-project bootie1

mvn fabric8:deploy

hit the openshift console to see the assigned route

Infinispan:
Now, to make this interesting

* Update src/main/resources/application.properties with
logging.level.org.jgroups=INFO

* Update pom.xml
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
		<!-- Infinispan properties BEGIN -->
		<infinispan-spring-boot-starter.version>1.0.0.CR1</infinispan-spring-boot-starter.version>
		<infinispan-bom.version>9.0.0.Final</infinispan-bom.version>
		<!-- Infinispan properties END -->
	</properties>

	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>org.infinispan</groupId>
				<artifactId>infinispan-bom</artifactId>
				<version>${infinispan-bom.version}</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
			<dependency>
				<groupId>org.infinispan</groupId>
				<artifactId>infinispan-spring-boot-starter</artifactId>
				<version>${infinispan-spring-boot-starter.version}</version>
			</dependency>
		</dependencies>
	</dependencyManagement>

        <!-- Infinispan Dependencies BEGIN -->
		<dependency>
			<groupId>org.infinispan</groupId>
			<artifactId>infinispan-spring-boot-starter</artifactId>
		</dependency>
		<dependency>
			<groupId>org.infinispan</groupId>
			<artifactId>infinispan-embedded</artifactId>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<!-- Infinispan Dependencies END -->


* oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default -n $(oc project -q)

* Add main/fabric8/helloinfinispan-deployment.yml

* Add src/main/java/com/burrsutter/InfinispanConfiguration.java

* Updated MyRESTController to have put, get and clear capabilities on the cache

mvn clean fabric8:deploy -DskipTests

By default, the helloinfinispan-deploy.yml requests 2 replicas

Kubernetes/OpenShift will perform the rolling update 

and anything in your cache will be maintained during and beyond the update

/addstuff?name=jim
Added jim to 2 helloinfinispan-7-087v1

/getstuff
RequestX for /getstuff on pod: helloinfinispan-7-087v1

    1 helloinfinispan-6-xfpns=george
    0 helloinfinispan-6-s0c22=mary
    2 helloinfinispan-7-087v1=burr

/clearstuff
  clears out the current cache entries

/curlstuff
makes it obvious that all pods see all the cache data
helloinfinispan-7-087v1 1 helloinfinispan-6-xfpns=george0 helloinfinispan-6-s0c22=mary2 helloinfinispan-7-087v1=burr
helloinfinispan-7-wbrzd 1 helloinfinispan-6-xfpns=george0 helloinfinispan-6-s0c22=mary2 helloinfinispan-7-087v1=burr

