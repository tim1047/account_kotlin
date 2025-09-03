package com.accountbook.division.dto

import java.math.BigDecimal
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DivisionSumResponseDto(
    val income: BigDecimal,
    val interest: BigDecimal,
    val expense: BigDecimal,
    val invest: BigDecimal,
    val investRate: String
)