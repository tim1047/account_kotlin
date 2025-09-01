
package com.accountbook.category.dto

import com.accountbook.dto.BaseDto
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategorySeqResponseDto (
    val categoryId: String,
    val categoryNm: String?,
    val categorySeq: String,
    val categorySeqNm: String,
) 