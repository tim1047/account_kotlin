
package com.accountbook.payment.dto

data class PaymentResponseDto (
    val paymentId: String,
    val paymentNm: String,
    val memberId: String,
    val paymentType: String,
    val useYn: String
) 