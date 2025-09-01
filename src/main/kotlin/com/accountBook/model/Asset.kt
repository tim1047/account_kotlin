package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("asset")
data class Asset (
    @Id
    val assetId: String,
    val assetNm: String,
) : BaseEntity()