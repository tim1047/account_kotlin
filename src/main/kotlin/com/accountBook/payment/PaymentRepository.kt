package com.accountbook.payment

import com.accountbook.model.Payment
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PaymentRepository : CoroutineCrudRepository<Payment, String> {
    
    @Query("SELECT * FROM payment")
    suspend fun getPayments(): List<Payment>

    @Query("SELECT * FROM payment WHERE 1=1 AND member_id = :memberId")
    suspend fun getPaymentsByMemberId(@Param("memberId") memberId: String?,): List<Payment>
}