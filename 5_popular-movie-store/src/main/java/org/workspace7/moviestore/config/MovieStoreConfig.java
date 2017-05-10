/*
 *  Copyright (c) 2017 Kamesh Sampath<kamesh.sampath@hotmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.workspace7.moviestore.config;

import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.spring.provider.SpringEmbeddedCacheManager;
import org.infinispan.spring.session.configuration.EnableInfinispanEmbeddedHttpSession;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.workspace7.moviestore.listeners.SessionsCacheListener;

import java.io.IOException;

/**
 * @author kameshs
 */
@EnableInfinispanEmbeddedHttpSession(cacheName = "moviestore-sessions-cache")
@Configuration
@EnableCaching
@EnableConfigurationProperties(MovieStoreProps.class)
public class MovieStoreConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SpringEmbeddedCacheManager cacheManager() throws IOException {
        return new SpringEmbeddedCacheManager(infinispanCacheManager());
    }

    public EmbeddedCacheManager infinispanCacheManager() throws IOException {
        EmbeddedCacheManager embeddedCacheManager = new DefaultCacheManager(this.getClass()
            .getResourceAsStream("/infinispan-moviestore.xml"));
        embeddedCacheManager
            .getCache("moviestore-sessions-cache")
            .addListener(new SessionsCacheListener());
        return embeddedCacheManager;
    }

}
