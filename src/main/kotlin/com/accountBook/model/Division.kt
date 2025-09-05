package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("division")
data class Division (
    @Id
    val divisionId: String,
    val divisionNm: String,
) : BaseEntity<String>() {
    override fun getId(): String? = divisionId

    override fun isNew(): Boolean = forceInsert
}