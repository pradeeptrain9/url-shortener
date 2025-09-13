package org.example.service;

import org.example.entity.UrlMapping;
import org.example.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final UrlMappingRepository repository;

    private final StringRedisTemplate redisTemplate;

    private final KeyGenerator keyGenerator;

    private static final String CACHE_PREFIX = "short:";



    @Transactional
    public String createShortUrl(String longUrl) {
        UrlMapping existing = repository.findByLongUrl(longUrl);
        if (existing != null) {
            return existing.getShortKey();
        }

        for (int attempt = 0; attempt < 5; attempt++) {
            String key = keyGenerator.generateKey(7);
            if (!repository.existsById(key)) {
                UrlMapping mapping = new UrlMapping();
                mapping.setShortKey(key);
                mapping.setLongUrl(longUrl);
                repository.save(mapping);

                redisTemplate.opsForValue().set(CACHE_PREFIX + key, longUrl);
                return key;
            }
        }
        throw new RuntimeException("Failed to generate unique key after retries");
    }

    public String resolveUrl(String key) {
        String cacheKey = CACHE_PREFIX + key;
        String cachedUrl = redisTemplate.opsForValue().get(cacheKey);
        if (cachedUrl != null) {
            System.out.print("Cache hit for key: " + key);
            return cachedUrl;
        }

        return repository.findById(key)
                .map(mapping -> {
                    redisTemplate.opsForValue().set(cacheKey, mapping.getLongUrl());
                    return mapping.getLongUrl();
                })
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }
}

