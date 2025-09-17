
package com.accountbook.utils

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.math.BigDecimal
import java.math.RoundingMode


@Service
class AssetUtils (
    private val webClient: WebClient.Builder,
    private val TWELVE_DATA_API_KEY: String = "aa19e92250cf43639ada8fca3a8c54b7",
    private val KRX_OPEN_API_KEY: String = "cc96dfdf193b4591cb499bd164f365776d218786df3282c56609b4af6db5cf1c",
    private val KRX_ETF_OPEN_API_KEY: String = "cc96dfdf193b4591cb499bd164f365776d218786df3282c56609b4af6db5cf1c"

) {
    data class ExchangeRateApiResponse(
        val rates: Map<String, Double>
    )
    
    suspend fun getExchangeRate(): Map<String, BigDecimal> {
        try {            
            val response: ExchangeRateApiResponse = webClient.build()
                .get()
                .uri("https://api.exchangerate-api.com/v4/latest/KRW")
                .retrieve()
                .awaitBody()
            
                return mapOf(
                "USD" to BigDecimal(1.0 / response.rates["USD"]!!).setScale(0, RoundingMode.DOWN),
                "JPY" to BigDecimal(1.0 / response.rates["JPY"]!!).setScale(2, RoundingMode.DOWN)
            )
            
        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }

    suspend fun getUSStockPrice(ticker: String): BigDecimal {
        try {
            val response: Map<String, String> = webClient.build()
                .get()
                .uri("https://api.twelvedata.com/price?symbol=$ticker&apikey=$TWELVE_DATA_API_KEY")
                .retrieve()
                .awaitBody()

            return BigDecimal(response["price"])
            
        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }

    suspend fun getKrxStockPrice(ticker: String): BigDecimal {
        try {
            var price = BigDecimal.ZERO

            val response: Map<String, Any> = webClient.build()
                .get()
                .uri("https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo?serviceKey=$KRX_OPEN_API_KEY&numOfRows=1&resultType=json&likeSrtnCd=$ticker")
                .retrieve()
                .awaitBody()
            
            val responseMap = response["response"] as Map<String, Any>
            val body = responseMap["body"] as Map<String, Any>
            val totalCount = body["totalCount"] as Int

            if (totalCount == 0) {
                price = this.getKrxEtfPrice(ticker)
            } else {
                val items = body["items"] as Map<String, Any>
                val itemList = items["item"] as List<Map<String, String>>
                val stockInfo = itemList.first()
                val clpr = stockInfo["clpr"] as String
                price = BigDecimal(clpr)
            }
            return price

        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }

    suspend fun getKrxEtfPrice(ticker: String): BigDecimal {
        try {
            val response: Map<String, Any> = webClient.build()
                .get()
                .uri("https://apis.data.go.kr/1160100/service/GetSecuritiesProductInfoService/getETFPriceInfo?serviceKey=$KRX_ETF_OPEN_API_KEY&numOfRows=1&resultType=json&likeSrtnCd=$ticker")
                .retrieve()
                .awaitBody()
            
            val responseMap = response["response"] as Map<String, Any>
            val body = responseMap["body"] as Map<String, Any>
            val items = body["items"] as Map<String, Any>
            val itemList = items["item"] as List<Map<String, String>>
            val stockInfo = itemList.first()
            val clpr = stockInfo["clpr"] as String
            return BigDecimal(clpr)

        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }

    suspend fun getCryptoPrice(ticker: String): BigDecimal {
        try {
            var symbol = ticker.lowercase()
            val response: Map<String, Map<String, BigDecimal>> = webClient.build()
                .get()
                .uri("https://api.coingecko.com/api/v3/simple/price?symbols=$symbol&vs_currencies=usd")
                .retrieve()
                .awaitBody()
            
            val priceMap = response[symbol]!!
            return priceMap["usd"]!!

        } catch (e: Exception) {
            throw RuntimeException(e.message, e)
        }
    }
}