package com.burrsutter;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
public class MyRESTController {

   final String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
   boolean markedForDeath = false;

   @RequestMapping("/")
    public String getStuff() {
        return "Stuff " + new java.util.Date() +  " on " + hostname + "\n";
    }

   @RequestMapping("/areyoualive")  
   public ResponseEntity<?> areyoualive() {
      System.out.println("liveness probe call on " + hostname + " at " + new java.util.Date());
      if (!markedForDeath) {
        return new ResponseEntity(HttpStatus.OK);
      } else {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }
    
   @RequestMapping("/areyouready")  
   public ResponseEntity<?> areyouready() {
      System.out.println("readniness probe call on " + hostname + " at " + new java.util.Date());       
      // verify a connection to a database
      // warm up caches
      // check with any sibling containers running in this same pod    
      if (!markedForDeath) {
        return new ResponseEntity(HttpStatus.OK);
       } else {
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
       }

   }

   @RequestMapping("/preparetodie") 
   public String preparetodie() {       
       markedForDeath = true;
       System.out.println(hostname + " was marked for DEATH!");
       return hostname + " markedForDeath = true";
   }
}