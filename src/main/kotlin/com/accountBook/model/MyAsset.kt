package com.accountbook.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.apache.logging.log4j.util.StringMap
import java.math.BigDecimal

@Table("my_asset")
data class MyAsset(
    @Id
    val myAssetId: String,
    val myAssetNm: String, 
    val assetId: String,
    val ticker: String,
    val priceDivCd: String,
    val price: BigDecimal,
    val qty: Double,
    val exchangeRateYn: String,
    val myAssetGroupId: String,
    val cashableYn: String
) : BaseEntity<String>() {
    override fun getId(): String? = myAssetId

    override fun isNew(): Boolean = forceInsert
}