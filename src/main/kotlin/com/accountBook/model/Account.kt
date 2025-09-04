package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.apache.logging.log4j.util.StringMap
import java.math.BigDecimal

@Table("account")
data class Account(
    @Id
    val accountId: Number,
    val accountDt: String,
    val divisionId: String,
    val memberId: String,
    val paymentId: String,
    val categoryId: String,
    val categorySeq: String,
    val price: BigDecimal,
    val remark: String,
    val impulseYn: String,
    val pointPrice: BigDecimal,
) : BaseEntity()