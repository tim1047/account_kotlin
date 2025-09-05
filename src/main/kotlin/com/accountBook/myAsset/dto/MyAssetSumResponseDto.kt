
package com.accountbook.myAsset.dto

import com.accountbook.dto.BaseDto
import com.accountbook.myAsset.dto.MyAssetSumDto
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MyAssetSumResponseDto (
    val accumDt: String,
    val data: List<MyAssetSumDto>,
    val sumPrice: BigDecimal
) 