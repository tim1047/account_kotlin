package com.accountbook.payment

import com.accountbook.model.Payment
import com.accountbook.payment.dto.PaymentResponseDto
import com.accountbook.dto.BaseResponseDto
import com.accountbook.payment.PaymentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("/payment")
class PaymentController (
    private val paymentService: PaymentService
) {

    @GetMapping("")
    suspend fun getPayments(
         @RequestParam(value = "memberId", required = false) memberId: String?,
    ): BaseResponseDto<List<PaymentResponseDto>> {
        return try {
            val payments = paymentService.getPayments(memberId)
            val responseDto = payments.map {
                dto -> PaymentResponseDto(
                    paymentId = dto.paymentId,
                    paymentNm = dto.paymentNm,
                    memberId = dto.memberId,
                    paymentType = dto.paymentType,
                    useYn = dto.useYn
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error("FAIL")
        }   
    }
}