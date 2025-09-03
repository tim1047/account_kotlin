
package com.accountbook.category.dto

import com.accountbook.dto.BaseDto
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategorySumResponseDto (
    val categoryId: String,
    val categoryNm: String?, 
    val divisionId: String?,
    val categorySeq: String?,
    val categorySeqNm: String?,
    val fixedPriceYn: String?,
    val sumPrice: Number?,
    val totalSumPrice: Number?,
    val data: MutableList<CategoryDtlSumDto> = ArrayList<CategoryDtlSumDto>(),
) 