package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("account_member")
data class Member (
    @Id
    val memberId: String,
    val memberNm: String,
) : BaseEntity<String>() {
    override fun getId(): String? = memberId

    override fun isNew(): Boolean = forceInsert
}