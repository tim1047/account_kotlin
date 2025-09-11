

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class AssetUtils (
    private val webClient: WebClient.Builder
) {
    data class ExchangeRateApiResponse(
        val result: String,
        val base_code: String,
        val conversion_rates: Map<String, Double>
    )
    
    suspend fun getExchangeRate() {
        try {
            val response: ExchangeRateApiResponse = webClient.build()
                .get()
                .uri("https://api.exchangerate-api.com/v4/latest/KRW")
                .retrieve().awaitBody()
            
            return mapOf(
                "USD" to BigDecimal(1.0 / response.conversion_rates["USD"]!!),
                "JPY" to BigDecimal(1.0 / response.conversion_rates["JPY"]!!)
            )
        } catch (e: Exception) {
            throw RuntimeException("환율 정보 조회 실패", e)
        }
    }
}