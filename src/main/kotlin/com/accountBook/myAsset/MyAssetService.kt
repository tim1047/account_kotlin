package com.accountbook.myAsset

import com.accountbook.myAsset.MyAssetRepository
import com.accountbook.myAsset.dto.MyAssetSumDto
import org.springframework.stereotype.Service
import kotlin.minus
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class MyAssetService(
    private val myAssetRepository: MyAssetRepository
) {
    
    suspend fun getMyAssetSum(procDt: String): List<MyAssetSumDto> {
        var myAssetSums = myAssetRepository.getMyAssetSum(procDt).toMutableList()
        var totalSumPrice = BigDecimal.ZERO

        for (myAssetSum in myAssetSums) {
            var sumPrice = myAssetSum.sumPrice

            if (AssetConstants.DEBT.equals(myAssetSum.assetId)) {
                sumPrice = sumPrice.minus(BigDecimal.valueOf(-1))
            }
            totalSumPrice = totalSumPrice.add(sumPrice)
        }
        myAssetSums.add(0, MyAssetSumDto(procDt, "0", "총 자산", totalSumPrice.setScale(0, RoundingMode.DOWN)))
        return myAssetSums
    }
}