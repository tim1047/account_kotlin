package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("divisions")
data class Division (
    @Id
    val divisionId: String,
    val divisionNm: String,
    val regpeId: String,
    val regDts: String,
    val modpeId: String,
    val modDts: String
)