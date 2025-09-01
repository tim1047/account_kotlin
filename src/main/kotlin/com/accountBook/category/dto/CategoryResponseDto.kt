
package com.accountbook.category.dto

import com.accountbook.dto.BaseDto
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategoryResponseDto (
    val categoryId: String,
    val categoryNm: String?,
    val divisionId: String?,
    val divisionNm: String?,
) 