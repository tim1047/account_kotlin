package com.accountbook.division.dto

import com.accountbook.dto.BaseDto
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DivisionSumGroupByMonthDto(
    val divisionId: String,
    val divisionNm: String,
    val sumPrice: BigDecimal?,
    val month: Number
)