
package com.accountbook.payment.dto

import com.accountbook.dto.BaseDto

data class PaymentDto (
    val paymentId: String,
    val paymentNm: String,
    val memberId: String,
    val paymentType: String,
    val useYn: String
) : BaseDto()