package com.accountbook.division.dto

import com.accountbook.dto.BaseDto


data class DivisionDto(
    val divisionId: String,
    val divisionNm: String,
) : BaseDto()