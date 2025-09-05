package com.accountbook.account.dto

import java.math.BigDecimal

data class CreateAccountRequestDto(
    val acoountDt: String,
    val divisionId: String,
    val memberId: String,
    val paymentId: String,
    val categoryId: String,
    val categorySeq: String = "",
    val price: BigDecimal,
    val remark: String = "",
    val impulseYn: String = "N",
    val pointPrice: BigDecimal = BigDecimal.ZERO    
)