
package com.accountbook.asset.dto

import com.accountbook.dto.BaseDto
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AssetResponseDto (
    val assetId: String,
    val assetNm: String
) 