package com.accountbook.division.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal
import java.math.RoundingMode


data class DivisionSumGroupByMonthResponseDto(
    val avgSumPrice: BigDecimal,
    val data: List<DivisionSumGroupByMonthDto>
) {
    constructor(data: List<DivisionSumGroupByMonthDto>) : this(
        avgSumPrice = if (data.isEmpty()) {
            BigDecimal.ZERO
        } else {
            data.map { it.sumPrice }
                .fold(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal(data.size), 0, RoundingMode.HALF_UP)
        },
        data = data
    )
}