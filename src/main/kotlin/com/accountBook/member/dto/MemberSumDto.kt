package com.accountbook.member.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal

data class MemberSumDto(
    val memberId: String,
    val memberNm: String,
    val sumPrice: BigDecimal
) : BaseDto()