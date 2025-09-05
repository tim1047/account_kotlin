
package com.accountbook.myAsset.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal

data class MyAssetDto (
    val myAssetId: String,
    val myAssetNm: String, 
    val assetId: String,
    val ticker: String,
    val priceDivCd: String,
    val price: BigDecimal,
    val qty: Double,
    val exchangeRateYn: String,
    val myAssetGroupId: String,
) : BaseDto()