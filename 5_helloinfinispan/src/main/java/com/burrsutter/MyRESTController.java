package com.burrsutter;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.infinispan.AdvancedCache;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.infinispan.health.HealthStatus;

@RestController
public class MyRESTController {
    final String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");

    @Autowired
    @Qualifier("cache1") // defined in InfinispanConfiguration.java
    AdvancedCache<String, String> cache1;

    @GetMapping("/")
    public String index() {
        System.out.println("/ invoked");
        return "Hello from Spring Boot! " + new java.util.Date() + " on " + hostname;
    }

    @GetMapping("/addstuff") 
    public String addStuff(@RequestParam(value="name", defaultValue="Burr") String name) {
        System.out.println("name:" + name);
        int currentSize = cache1.keySet().size();
        // using the size as a way to provide uniqueness to keys
        String key = currentSize + "_" + hostname;
        cache1.put(key, name);        
        return "Added " + name + " to " + key;        
    }
    @GetMapping("/clearstuff") 
    public String clearStuff() {
        cache1.clear();
        return "Cleared";        
    }

    @GetMapping("/getstuff") 
    public String getStuff() {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = cache1.keySet();
        sb.append("BE Request for /getstuff on pod: " + hostname);
        sb.append("<UL>");
        for (String key : keySet) {
            String value = cache1.get(key);
            System.out.println("k: " + key + " v: " + value);
            sb.append("<LI>");
            sb.append(key + "=" + value);
            sb.append("</LI>");
        } // for
        sb.append("</UL>");
        return sb.toString();
    }
    @GetMapping("/curlstuff") 
    public String curlStuff() {
        StringBuilder sb = new StringBuilder();
        sb.append(hostname + " BE ");
        Set<String> keySet = cache1.keySet();
        for (String key : keySet) {
            String value = cache1.get(key);
            sb.append(key + "=" + value);
        }
        sb.append("\n");
        return sb.toString();
    }    

   @GetMapping("/aliveandwell")  
   public ResponseEntity<?> aliveandwell() {
      System.out.println("liveness probe call on " + hostname + " at " + new java.util.Date());
      return new ResponseEntity(HttpStatus.OK);
   }

   @GetMapping("/readyornot")  
   public ResponseEntity<?> readyornot() {
      System.out.println("readniness probe call on " + hostname + " at " + new java.util.Date());
      
      HealthStatus healthStatus = cache1.getCacheManager().getHealth().getClusterHealth().getHealthStatus();
      if (healthStatus == HealthStatus.HEALTHY) {
            System.out.println("HEALTHY");
            return new ResponseEntity(HttpStatus.OK);
      } else {
          System.out.println("NOT Healthy");
          return new ResponseEntity(HttpStatus.NOT_FOUND);
      }
      
   }

}