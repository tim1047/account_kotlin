
package com.accountbook.myAsset.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal

data class MyAssetSumDto (
    var accumDt: String?,
    val assetId: String,
    val assetNm: String, 
    val sumPrice: BigDecimal,
)