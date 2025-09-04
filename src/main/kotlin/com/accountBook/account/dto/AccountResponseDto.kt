
package com.accountbook.account.dto

import com.accountbook.dto.BaseDto
import java.math.BigDecimal

data class AccountResponseDto (
    val seq: Number,
    val accountId: Number, 
    val accountDt: String,
    val divisionId: String,
    val divisionNm: String,
    val memberId: String,
    val memberNm: String,
    val paymentId: String,
    val paymentNm: String,
    val paymentType: String,
    val categoryId: String,
    val categoryNm: String,
    val categorySeq: String?,
    val categorySeqNm: String?,
    val price: BigDecimal,
    val remark: String?,
    val impulseYn: String,
    val pointPrice: BigDecimal = BigDecimal.ZERO
)