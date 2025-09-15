package com.accountbook.myAsset.dto

import java.math.BigDecimal

data class UpdateMyAssetRequestDto(
    val myAssetNm: String,
    val assetId: String,
    val ticker: String,
    val priceDivCd: String,
    val price: BigDecimal,
    val qty: Double,
    val exchangeRateYn: String,
    val cashableYn: String 
)