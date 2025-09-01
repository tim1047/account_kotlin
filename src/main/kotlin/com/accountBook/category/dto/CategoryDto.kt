
package com.accountbook.category.dto

import com.accountbook.dto.BaseDto

data class CategoryDto (
    val categoryId: String,
    val categoryNm: String?, 
    val divisionId: String?,
    val divisionNm: String?,
    val categorySeq: String?,
    val categorySeqNm: String?,
    val fixedPriceYn: String?
) : BaseDto()