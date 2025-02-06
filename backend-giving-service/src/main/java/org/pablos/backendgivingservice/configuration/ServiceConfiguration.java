package org.pablos.backendgivingservice.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.pablos.common.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;

/**
 * Конфигурация сервиса
 */
@Getter
@Configuration
public class ServiceConfiguration {

    /**
     * Длина сокращённой ссылки
     */
    @Value("${properties.short_link_length}")
    private int shortLinkLength;

    /**
     * Максимальная длина сокращаемой ссылки
     */
    @Value("${properties.full_link_max_length}")
    private int fullLinkMaxLength;

    /**
     * Возвращает логгер для записи сообщений
     */
    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger(getClass());
    }

    /**
     * Возвращает конфигурацию Redis-кэша
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    /**
     * Возвращает настройщик RedisCacheManagerBuilder
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration("fullLink",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)));
    }

    /**
     * Инициализация длин ссылок.
     */
    @PostConstruct
    public void init() {
        CommonUtil.FULL_LINK_MAX_LENGTH = fullLinkMaxLength;
        CommonUtil.SHORT_LINK_LENGTH = shortLinkLength;
    }

}
