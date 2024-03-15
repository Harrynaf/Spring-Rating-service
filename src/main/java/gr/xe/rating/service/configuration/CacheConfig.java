package gr.xe.rating.service.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("ratingsCache");
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS));//Used Seconds instead of minutes for testing
        return cacheManager;
    }
}
