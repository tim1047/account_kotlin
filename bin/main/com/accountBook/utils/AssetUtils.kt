
package com.accountbook.utils

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.math.BigDecimal

@Service
class AssetUtils (
    private val webClient: WebClient.Builder
) {
    data class ExchangeRateApiResponse(
        val rates: Map<String, Double>
    )
    
    
    suspend fun getExchangeRate(): Map<String, BigDecimal> {
        try {
            val response: ExchangeRateApiResponse = webClient.build()
                .get()
                .uri("https://api.exchangerate-api.com/v4/latest/KRW")
                .retrieve().awaitBody()
            return mapOf(
                "USD" to BigDecimal(1.0 / response.rates["USD"]!!),
                "JPY" to BigDecimal(1.0 / response.rates["JPY"]!!)
            )
            
        } catch (e: Exception) {
            throw RuntimeException("환율 정보 조회 실패", e)
        }
    }
}