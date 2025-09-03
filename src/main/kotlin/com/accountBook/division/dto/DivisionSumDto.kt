package com.accountbook.division.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal


data class DivisionSumDto(
    val income: BigDecimal,
    val interest: BigDecimal,
    val expense: BigDecimal,
    val invest: BigDecimal,
    val investRate: String
) : BaseDto()