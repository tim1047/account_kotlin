package com.accountbook.division.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal


data class DivisionSumDailyDto(
    val accountDt: String,
    val accountYYYYMM: String,
    val accountDD: String,
    val sumPrice: BigDecimal?
)