
package com.accountbook.myAsset.dto

import com.accountbook.dto.BaseDto
import com.accountbook.myAsset.dto.MyAssetTotalDto
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MyAssetResponseDto (
    var data: MutableMap<String, MyAssetTotalDto> = mutableMapOf(),
    var totSumPrice: BigDecimal = BigDecimal.ZERO,
    var totNetWorthSumPrice: BigDecimal = BigDecimal.ZERO,
    var totCashableSumPrice: BigDecimal = BigDecimal.ZERO,
    var usdKrwRate: BigDecimal = BigDecimal.ZERO,
    var jpyKrwRate: BigDecimal = BigDecimal.ZERO,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var myAssetAccumDts: LocalDateTime? =  LocalDateTime.now()
) {
    fun createData(myAssetList: List<MyAssetTotalDto>) {
        for (myAsset in myAssetList) {
            this.data[myAsset.assetId] = myAsset
            this.totSumPrice = this.totSumPrice.add(myAsset.assetTotSumPrice)
            this.totNetWorthSumPrice = this.totNetWorthSumPrice.add(myAsset.assetNetWorthSumPrice)
            this.totCashableSumPrice = this.totCashableSumPrice.add(myAsset.assetCashableSumPrice)
        }    
        this.myAssetAccumDts = myAssetList[0].myAssetAccumDts
    }
}