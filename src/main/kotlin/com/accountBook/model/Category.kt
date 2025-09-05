package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("category")
data class Category (
    @Id
    val categoryId: String,
    val categoryNm: String,
) : BaseEntity<String>() {
    override fun getId(): String? = categoryId

    override fun isNew(): Boolean = forceInsert
}