
package com.accountbook.myAsset.dto

import com.accountbook.dto.BaseDto
import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class MyAssetDto (
    val myAssetId: String,
    val myAssetNm: String, 
    val assetId: String,
    val assetNm: String,
    val ticker: String,
    val priceDivCd: String,
    val price: BigDecimal,
    val qty: Double,
    var sumPrice: BigDecimal? = BigDecimal.ZERO,
    val exchangeRateYn: String,
    val myAssetGroupId: String,
    val myAssetGroupNm: String? = "",
    val cashableYn: String,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    val myAssetAccumDts: LocalDateTime? =  LocalDateTime.now()
)

data class MyAssetGroupDto (
    var myAssetGroupId: String,
    var myAssetGroupNm: String,
    var myAssetNm: String,
    var assetId: String,
    var assetNm: String,
    var sumPrice: BigDecimal,
    // var qty: Double,
    var data: MutableList<MyAssetDto>
)

data class MyAssetTotalDto (
    var assetNm: String,
    var assetTotSumPrice: BigDecimal,
    var assetId: String,
    var assetNetWorthSumPrice: BigDecimal,
    var assetCashableSumPrice: BigDecimal, 
    var data: MutableList<Any>,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var myAssetAccumDts: LocalDateTime? =  LocalDateTime.now()
)