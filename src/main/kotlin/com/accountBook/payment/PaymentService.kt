package com.accountbook.payment

import com.accountbook.payment.dto.PaymentDto
import com.accountbook.payment.PaymentRepository
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository
) {
    
    suspend fun getPayments(memberId: String? = null): List<PaymentDto> {
        val entites = when {
            memberId != null -> {
                paymentRepository.getPaymentsByMemberId(memberId)
            }
            else -> {
                paymentRepository.getPayments()
            }
        }
        return entites.map { payment ->
                PaymentDto(
                    paymentId = payment.paymentId,
                    paymentNm = payment.paymentNm,
                    memberId = payment.memberId,
                    paymentType = payment.paymentType,
                    useYn = payment.useYn
                )
            }
    }
}