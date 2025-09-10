package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.apache.logging.log4j.util.StringMap
import java.math.BigDecimal
import jakarta.persistence.*

@Entity
@Table("account")
data class Account(
    @Id
    val accountId: Long,
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
) : BaseEntity<Long>() {
    override fun getId(): Long? = accountId

    override fun isNew(): Boolean = forceInsert || accountId == null
}