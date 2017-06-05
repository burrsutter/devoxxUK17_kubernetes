package com.burrsutter;

import org.infinispan.AdvancedCache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import infinispan.autoconfigure.embedded.InfinispanCacheConfigurer;
import infinispan.autoconfigure.embedded.InfinispanGlobalConfigurer;

@Configuration
public class InfinispanConfiguration {

   @Bean
   public InfinispanGlobalConfigurer globalConfiguration() {
      return () ->
            // Here you define global things, such as JGroups stack, JMX configuration etc.
            GlobalConfigurationBuilder
               .defaultClusteredBuilder()
                  // Use this line for testing in Kubernetes. But it requires additional configuration:
                  // oc policy add-role-to-user view system:serviceaccount:$(oc project -q):default -n $(oc project -q)
                  // And setting OPENSHIFT_KUBE_PING_NAMESPACE env variable to your namespace
               .transport().defaultTransport().addProperty("configurationFile", "default-configs/default-jgroups-kubernetes.xml")

                  // Or use, multicast stack to simplify local testing:
//                  .transport().defaultTransport().addProperty("configurationFile", "jgroups-config.xml")
               .build();
   }

   @Bean
   public InfinispanCacheConfigurer cacheConfigurer() {
      return embeddedCacheManager -> {
         // And here are per-cache configuration, e.g. eviction, replication scheme etc.
         ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
         configurationBuilder.clustering().cacheMode(CacheMode.DIST_SYNC);
         configurationBuilder.memory().eviction().strategy(EvictionStrategy.LRU).size(100);

         embeddedCacheManager.defineConfiguration("cache1", configurationBuilder.build());
         embeddedCacheManager.defineConfiguration("cache2", configurationBuilder.build());
      };
   }

   @Bean
   @Qualifier("cache1")
   public AdvancedCache<String, String> cache1(EmbeddedCacheManager cacheManager) {
      return cacheManager.<String, String>getCache("cache1").getAdvancedCache();
   }

   @Bean
   @Qualifier("cache2")
   public AdvancedCache<String, String> cache2(EmbeddedCacheManager cacheManager) {
      return cacheManager.<String, String>getCache("cache2").getAdvancedCache();
   }

}