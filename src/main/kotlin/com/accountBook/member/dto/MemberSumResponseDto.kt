package com.accountbook.member.dto

import java.math.BigDecimal

data class MemberSumResponseDto(
    val memberId: String,
    val memberNm: String,
    val sumPrice: BigDecimal
)