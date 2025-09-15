package com.accountbook.myAsset

import com.accountbook.myAsset.MyAssetRepository
import com.accountbook.myAsset.dto.MyAssetSumDto
import com.accountbook.myAsset.dto.MyAssetTotalDto
import com.accountbook.myAsset.dto.MyAssetGroupDto
import com.accountbook.myAsset.dto.MyAssetDto
import com.accountbook.myAsset.dto.UpdateMyAssetRequestDto
import com.accountbook.myAsset.dto.CreateMyAssetRequestDto
import com.accountbook.account.dto.CreateAccountRequestDto
import com.accountbook.model.MyAsset
import org.springframework.stereotype.Service
import kotlin.minus
import java.math.BigDecimal
import java.math.RoundingMode
import AssetConstants

@Service
class MyAssetService(
    private val myAssetRepository: MyAssetRepository
) {

    suspend fun getMyAssetList(procDt: String): List<MyAssetTotalDto> {
        val myAssetList = myAssetRepository.getMyAssetList(procDt)
        return this.summaryMyAssetList(myAssetList)
    }

    suspend fun summaryMyAssetList(myAssetList: List<MyAssetDto>): List<MyAssetTotalDto> {
        var myAssetTotalDtoMap: MutableMap<String, MyAssetTotalDto> = mutableMapOf()
        var myAssetGroupDtoMap: MutableMap<String, MyAssetGroupDto> = mutableMapOf()

        for (myAsset in myAssetList) {
            var sumPrice = myAsset.sumPrice
            var netWorthSumPrice = myAsset.sumPrice
            var cashableSumPrice = BigDecimal.ZERO

            if (AssetConstants.DEBT.equals(myAsset.assetId)) {
                netWorthSumPrice = netWorthSumPrice.multiply(BigDecimal.valueOf(-1))
            }
            if ("Y".equals(myAsset.cashableYn)) {
                cashableSumPrice = sumPrice
            }

            if (!myAssetTotalDtoMap.containsKey(myAsset.assetId)) {
                myAssetTotalDtoMap[myAsset.assetId] = MyAssetTotalDto(
                    assetNm = myAsset.assetNm,
                    assetTotSumPrice = BigDecimal.ZERO, 
                    assetId = myAsset.assetId, 
                    assetNetWorthSumPrice = BigDecimal.ZERO,
                    assetCashableSumPrice = BigDecimal.ZERO,
                    data = mutableListOf()
                )
            }
            myAssetTotalDtoMap[myAsset.assetId]!!.assetTotSumPrice = myAssetTotalDtoMap[myAsset.assetId]!!.assetTotSumPrice.add(sumPrice)
            myAssetTotalDtoMap[myAsset.assetId]!!.assetNetWorthSumPrice = myAssetTotalDtoMap[myAsset.assetId]!!.assetNetWorthSumPrice.add(netWorthSumPrice)
            myAssetTotalDtoMap[myAsset.assetId]!!.assetCashableSumPrice = myAssetTotalDtoMap[myAsset.assetId]!!.assetCashableSumPrice.add(cashableSumPrice)
            myAssetTotalDtoMap[myAsset.assetId]!!.myAssetAccumDts = myAsset.myAssetAccumDts

            if (!"0".equals(myAsset.myAssetGroupId)) {
                if (!myAssetGroupDtoMap.containsKey(myAsset.myAssetGroupId)) {
                    myAssetGroupDtoMap[myAsset.myAssetGroupId] = MyAssetGroupDto(
                        myAssetGroupId = myAsset.myAssetGroupId,
                        myAssetGroupNm = myAsset.myAssetGroupNm!!,
                        myAssetNm = myAsset.myAssetNm,
                        assetId = myAsset.assetId,
                        sumPrice = BigDecimal.ZERO,
                        data = mutableListOf()
                    )
                }
                myAssetGroupDtoMap[myAsset.myAssetGroupId]!!.sumPrice = myAssetGroupDtoMap[myAsset.myAssetGroupId]!!.sumPrice.add(myAsset.sumPrice)
                myAssetGroupDtoMap[myAsset.myAssetGroupId]!!.data.add(myAsset)
            } else {
                myAssetTotalDtoMap[myAsset.assetId]!!.data.add(myAsset) 
            }
            
        }

        myAssetGroupDtoMap.forEach { (_, value) ->
            myAssetTotalDtoMap[value.assetId]!!.data.add(value)
        }
        return myAssetTotalDtoMap.values.toList()
    }

    suspend fun createMyAssetList(procDt: String): List<MyAssetTotalDto> {
        var myAssetList: List<MyAssetDto> = listOf()
        return summaryMyAssetList(myAssetList)
    }

    suspend fun getMyAssetSum(procDt: String): List<MyAssetSumDto> {
        var myAssetSums = myAssetRepository.getMyAssetSum(procDt).toMutableList()
        var totalSumPrice = BigDecimal.ZERO

        for (myAssetSum in myAssetSums) {
            var sumPrice = myAssetSum.sumPrice

            if (AssetConstants.DEBT.equals(myAssetSum.assetId)) {
                sumPrice = sumPrice.minus(BigDecimal.valueOf(-1))
            }
            totalSumPrice = totalSumPrice.add(sumPrice)
            myAssetSum.accumDt = procDt
        }
        myAssetSums.add(0, MyAssetSumDto(procDt, "0", "총 자산", totalSumPrice.setScale(0, RoundingMode.DOWN)))
        return myAssetSums
    }

    suspend fun insertMyAsset(createMyAssetRequestDto: CreateMyAssetRequestDto) {
        var myAssetId = myAssetRepository.getMyAssetSeq()

        val myAssetEntity = MyAsset(
            myAssetId=myAssetId.toString(),
            myAssetNm=createMyAssetRequestDto.myAssetNm,
            assetId=createMyAssetRequestDto.assetId,
            ticker=createMyAssetRequestDto.ticker,
            priceDivCd=createMyAssetRequestDto.priceDivCd,
            price=createMyAssetRequestDto.price,
            qty=createMyAssetRequestDto.qty,
            exchangeRateYn=createMyAssetRequestDto.exchangeRateYn,
            myAssetGroupId="0",
            cashableYn=createMyAssetRequestDto.cashableYn
        ).apply { forceInsert = true }
        myAssetRepository.save(myAssetEntity)
    }

    suspend fun updateMyAsset(myAssetId: String, updateMyAssetRequestDto: UpdateMyAssetRequestDto) {
        val existingMyAsset = myAssetRepository.findById(myAssetId)
            ?: throw Exception("MyAsset not found: $myAssetId")
        
        val updatedMyAsset = existingMyAsset.copy(
            myAssetNm = updateMyAssetRequestDto.myAssetNm ?: existingMyAsset.myAssetNm,
            assetId = updateMyAssetRequestDto.assetId ?: existingMyAsset.assetId,
            ticker = updateMyAssetRequestDto.ticker ?: existingMyAsset.ticker,
            priceDivCd = updateMyAssetRequestDto.priceDivCd ?: existingMyAsset.priceDivCd,
            price = updateMyAssetRequestDto.price ?: existingMyAsset.price,
            qty = updateMyAssetRequestDto.qty ?: existingMyAsset.qty,
            exchangeRateYn = updateMyAssetRequestDto.exchangeRateYn ?: existingMyAsset.exchangeRateYn,
            myAssetGroupId = existingMyAsset.myAssetGroupId,
            cashableYn = updateMyAssetRequestDto.cashableYn ?: existingMyAsset.cashableYn
        )
        myAssetRepository.save(updatedMyAsset)
    }

    suspend fun deleteMyAsset(myAssetId: String) {
        myAssetRepository.deleteById(myAssetId)
    }
}