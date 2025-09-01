package com.accountbook.member.dto

import com.accountbook.dto.BaseDto

data class MemberDto(
    val memberId: String,
    val memberNm: String,
) : BaseDto()