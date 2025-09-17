package com.accountbook.myAsset

import com.accountbook.myAsset.MyAssetRepository
import com.accountbook.myAsset.MyAssetAccumRepository
import com.accountbook.myAsset.dto.MyAssetSumDto
import com.accountbook.myAsset.dto.MyAssetTotalDto
import com.accountbook.myAsset.dto.MyAssetGroupDto
import com.accountbook.myAsset.dto.MyAssetDto
import com.accountbook.myAsset.dto.UpdateMyAssetRequestDto
import com.accountbook.myAsset.dto.CreateMyAssetRequestDto
import com.accountbook.account.dto.CreateAccountRequestDto
import com.accountbook.model.MyAsset
import com.accountbook.model.MyAssetAccum
import com.accountbook.utils.AssetUtils
import com.accountbook.utils.DateUtils
import org.springframework.stereotype.Service
import kotlin.minus
import java.math.BigDecimal
import java.math.RoundingMode
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import AssetConstants

@Service
class MyAssetService(
    private val myAssetRepository: MyAssetRepository,
    private val myAssetAccumRepository: MyAssetAccumRepository,
    private val assetUtils: AssetUtils,
    private val dateUtils: DateUtils
) {

    suspend fun getMyAssetAccumList(procDt: String): List<MyAssetTotalDto> {
        val myAssetList = myAssetRepository.getMyAssetAccumList(procDt)
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
                netWorthSumPrice = netWorthSumPrice?.multiply(BigDecimal.valueOf(-1))
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

    suspend fun refreshMyAssetList(procDt: String): List<MyAssetTotalDto> {
        // my_asset 리스트 조회
        var myAssetList: List<MyAssetDto> = myAssetRepository.getMyAssetList(myAssetId="")

        // 자산 가격 실시간 조회 후, update
        var resultList = this.refreshMyAssetPrice(myAssetList)

        // my_asset_accum delete
        myAssetAccumRepository.deleteByAccumDt(procDt)

        // my_asset_accum insert
        val myAssetAccumEntities = resultList.map { 
            dto -> MyAssetAccum(
                accumDt=procDt,
                myAssetId=dto.myAssetId,
                myAssetNm=dto.myAssetNm,
                assetId=dto.assetId,
                ticker=dto.ticker,
                price=dto.price,
                qty=dto.qty,
            ).apply{
                forceInsert = true
            }
        }

        for (myAssetAccumEntity in myAssetAccumEntities) {
            myAssetAccumRepository.save(myAssetAccumEntity)
        }
        return this.summaryMyAssetList(resultList)
    }

    suspend fun refreshMyAssetPrice(myAssetList: List<MyAssetDto>): List<MyAssetDto> {
        val refreshMyAssetList = this.updateAssetPriceAsync(myAssetList)

        var resultList: MutableList<MyAssetDto> = mutableListOf()

        val exchageRateInfo = assetUtils.getExchangeRate()
        val usdKrwRate = exchageRateInfo["USD"]!!
        val jpyKrwRate = exchageRateInfo["JPY"]!!

        for (refreshMyAsset in refreshMyAssetList) {
            var price = refreshMyAsset.price
            var sumPrice = refreshMyAsset.sumPrice

            if ("Y".equals(refreshMyAsset.exchangeRateYn)) {
                if (AssetConstants.JAPAN_STOCK.equals(refreshMyAsset.assetId)) {
                    price = price.multiply(jpyKrwRate)
                    sumPrice = sumPrice?.multiply(jpyKrwRate)
                } else {
                    price = price.multiply(usdKrwRate)
                    sumPrice = sumPrice?.multiply(usdKrwRate)
                }
            }
            resultList.add(refreshMyAsset.copy(price=price.setScale(0, RoundingMode.DOWN), sumPrice=sumPrice?.setScale(0, RoundingMode.DOWN)))
        }
        return resultList
    }

    suspend fun updateAssetPriceAsync(myAssetList: List<MyAssetDto>): List<MyAssetDto> = coroutineScope {
        myAssetList.map { myAsset ->
            async {
                updateAssetPrice(myAsset)
            }
        }.awaitAll()
    }

    suspend fun updateAssetPrice(myAssetDto: MyAssetDto): MyAssetDto {
        var sumPrice: BigDecimal
        var price = myAssetDto.price
        val qty = myAssetDto.qty

        if ("AUTO".equals(myAssetDto.priceDivCd)) {
            price = when (myAssetDto.assetId) {
                AssetConstants.KOREA_STOCK -> assetUtils.getKrxStockPrice(myAssetDto.ticker)
                AssetConstants.USA_STOCK -> assetUtils.getUSStockPrice(myAssetDto.ticker)
                AssetConstants.COIN -> assetUtils.getCryptoPrice(myAssetDto.ticker)
                AssetConstants.PENSION -> assetUtils.getKrxStockPrice(myAssetDto.ticker)
                else -> myAssetDto.price
            }
        }
        sumPrice = price.multiply(BigDecimal(qty))
        return myAssetDto.copy(sumPrice = sumPrice.setScale(0, RoundingMode.DOWN), price=price.setScale(0, RoundingMode.DOWN))
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

        // my_asset_id 로 조회 
        var myAssetList: List<MyAssetDto> = myAssetRepository.getMyAssetList(myAssetId=myAssetId.toString())

        // 가격 계산
        var resultList = this.refreshMyAssetPrice(myAssetList)

        if (resultList.isNotEmpty()) {
            val myAsset = resultList.last()

            // insert my_asset_accum
            val myAssetAccumEntity =  MyAssetAccum(
                accumDt=dateUtils.getCurrentDt(),
                myAssetId=myAsset.myAssetId,
                myAssetNm=myAsset.myAssetNm,
                assetId=myAsset.assetId,
                ticker=myAsset.ticker,
                price=myAsset.price,
                qty=myAsset.qty,
            ).apply{
                forceInsert = true
            }
            myAssetAccumRepository.save(myAssetAccumEntity)
        }
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

        // my_asset_id 로 조회 
        var myAssetList: List<MyAssetDto> = myAssetRepository.getMyAssetList(myAssetId=myAssetId)

        // 가격 계산
        var resultList = this.refreshMyAssetPrice(myAssetList)

        if (resultList.isNotEmpty()) {
            val myAsset = resultList.last()

            // update my_asset_accum
            val myAssetAccumEntity = MyAssetAccum(
                accumDt=dateUtils.getCurrentDt(),
                myAssetId=myAsset.myAssetId,
                myAssetNm=myAsset.myAssetNm,
                assetId=myAsset.assetId,
                ticker=myAsset.ticker,
                price=myAsset.price,
                qty=myAsset.qty,
            )
            myAssetAccumRepository.updateMyAssetAcuumByPrimaryKey(myAssetAccumEntity)
        }
    }

    suspend fun deleteMyAsset(myAssetId: String) {
        myAssetRepository.deleteById(myAssetId)
        myAssetAccumRepository.deleteByAccumDtAndMyAssetId(dateUtils.getCurrentDt(), myAssetId)
    }
}