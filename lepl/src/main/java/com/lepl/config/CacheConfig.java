package com.lepl.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

    @Override
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("members");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(50) // 내부 해시 테이블의 최소한의 크기
                .maximumSize(50) // 캐시에 포함할 수 있는 최대 엔트리 수
//                .weakKeys() // 직접 키를 설정하므로 주석처리
                .recordStats());
        return cacheManager;
    }

    @Bean
    public CacheManager cacheManager2() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("users");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(1) // 내부 해시 테이블의 최소한의 크기 (캐릭터 어차피 1개만 기록)
                .maximumSize(200) // 캐시에 포함할 수 있는 최대 엔트리 수 (멤버 200명 정도한테 적용하자)
//                .weakKeys() // 직접 키를 설정하므로 주석처리
                .recordStats());
        return cacheManager;
    }
}
