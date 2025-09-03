
package com.accountbook.category.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal

data class CategorySumDto (
    val categoryId: String,
    val categoryNm: String?, 
    val divisionId: String?,
    val categorySeq: String?,
    val categorySeqNm: String?,
    val fixedPriceYn: String?,
    val sumPrice: BigDecimal,
    val totalSumPrice: BigDecimal,
    val data: MutableList<CategoryDtlSumDto> = ArrayList<CategoryDtlSumDto>(),
) : BaseDto() {
    fun createData(categorySeq: String, categorySeqNm: String, sumPrice: BigDecimal) {
        val newData = CategoryDtlSumDto(
            categorySeq = categorySeq,
            categorySeqNm = categorySeqNm,
            sumPrice = sumPrice
        )
        this.data.add(newData)            
    }
}

data class CategoryDtlSumDto (
    val categorySeq: String,
    val categorySeqNm: String,
    val sumPrice: BigDecimal
)