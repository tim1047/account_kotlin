package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.apache.logging.log4j.util.StringMap
import java.math.BigDecimal

@Table("my_asset_accum")
data class MyAssetAccum(
    val accumDt: String,
    val myAssetId: String,
    val myAssetNm: String, 
    val assetId: String,
    val ticker: String,
    val price: BigDecimal,
    val qty: Double,
) : BaseEntity<String>() {
    override fun getId(): String? = myAssetId + accumDt

    override fun isNew(): Boolean = forceInsert
}