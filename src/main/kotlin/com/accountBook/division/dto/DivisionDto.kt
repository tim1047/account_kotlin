package com.accountbook.division.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal


data class DivisionDto(
    val divisionId: String,
    val divisionNm: String,
    val sumPrice: BigDecimal?
) : BaseDto()