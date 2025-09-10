package com.accountbook.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class CorsConfig : WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")          // 모든 경로
            .allowedOrigins("*")           // 모든 도메인
            .allowedMethods("*")           // 모든 HTTP 메서드
            .allowedHeaders("*")           // 모든 헤더
            .maxAge(3600)                  // 1시간 캐시
    }
}