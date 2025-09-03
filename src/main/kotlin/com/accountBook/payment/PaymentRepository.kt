package com.accountbook.payment

import com.accountbook.model.Payment
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import PaymentQuery

@Repository
interface PaymentRepository : CoroutineCrudRepository<Payment, String> {
    
    @Query(PaymentQuery.GET_PAYMENTS)
    suspend fun getPayments(): List<Payment>

    @Query(PaymentQuery.GET_PAYMENTS_BY_MEMBER_ID)
    suspend fun getPaymentsByMemberId(@Param("memberId") memberId: String?,): List<Payment>
}