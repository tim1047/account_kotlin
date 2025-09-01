package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("payment")
data class Payment (
    @Id
    val paymentId: String,
    val paymentNm: String,
    val memberId: String,
    val paymentType: String,
    val useYn: String
) : BaseEntity()