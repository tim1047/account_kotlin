package com.accountbook.myAsset

import com.accountbook.dto.BaseResponseDto
import com.accountbook.myAsset.dto.MyAssetSumResponseDto
import com.accountbook.myAsset.dto.MyAssetResponseDto
import com.accountbook.myAsset.dto.UpdateMyAssetRequestDto
import com.accountbook.myAsset.dto.CreateMyAssetRequestDto
import com.accountbook.utils.AssetUtils
import com.accountbook.utils.DateUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/my-asset")
class MyAssetController (
    private val myAssetService: MyAssetService,
    private val assetUtils: AssetUtils,
    private val dateUtils: DateUtils
) {

    @GetMapping("")
    suspend fun getMyAsset(
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
    ): BaseResponseDto<MyAssetResponseDto> {
        return try {
            var myAssetList = myAssetService.getMyAssetAccumList(dateUtils.convertYYYYMMDDToYYYYMM(strtDt))
            
            var myAssetResponseDto = MyAssetResponseDto()
            myAssetResponseDto.createData(myAssetList)
            
            val exchageRateInfo = assetUtils.getExchangeRate()
            myAssetResponseDto.usdKrwRate = exchageRateInfo["USD"]!!
            myAssetResponseDto.jpyKrwRate = exchageRateInfo["JPY"]!!

            BaseResponseDto.success(myAssetResponseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }
    }

    @GetMapping("/sum")
    suspend fun getMysAssetSum(
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
    ): BaseResponseDto<List<MyAssetSumResponseDto>> {
        return try {
            var resultList = ArrayList<MyAssetSumResponseDto>();
            
            val startDate = this.dateUtils.convertDtToDate(strtDt)
            val endDate = this.dateUtils.convertDtToDate(endDt)

            var currentMonth = this.dateUtils.getCurrentYearMonth(startDate)
            val endMonth = this.dateUtils.getCurrentYearMonth(endDate)

            while (!currentMonth.isAfter(endMonth)) {
                var procDt = this.dateUtils.convertYearMonthToDt(currentMonth)

                val myAssetSums = myAssetService.getMyAssetSum(procDt)

                resultList.add(MyAssetSumResponseDto(procDt, myAssetSums, myAssetSums.get(0).sumPrice))
                currentMonth = currentMonth.plusMonths(1)
            }
            BaseResponseDto.success(resultList)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }   
    }
    
    @PostMapping("")
    suspend fun insertMyAsset(
        @RequestBody request: CreateMyAssetRequestDto
    ): BaseResponseDto<String> {
        return try {
            myAssetService.insertMyAsset(request)
            BaseResponseDto.success("")
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }
    }

    @PutMapping("/{myAssetId}")
    suspend fun updateMyAsset(
        @PathVariable myAssetId: String,
        @RequestBody updateMyAssetRequestDto: UpdateMyAssetRequestDto
    ): BaseResponseDto<String> {
        return try {
            myAssetService.updateMyAsset(myAssetId, updateMyAssetRequestDto)
            BaseResponseDto.success("")
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }
    }

    @DeleteMapping("/{myAssetId}")
    suspend fun deleteMyAsset(
        @PathVariable myAssetId: String
    ): BaseResponseDto<String> {
        return try {
            myAssetService.deleteMyAsset(myAssetId)
            BaseResponseDto.success("")
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }
    }

    @PostMapping("/refresh")
    suspend fun refreshMyAssetList(
        @RequestBody request: Map<String, Any>
    ): BaseResponseDto<MyAssetResponseDto> {
        return try {
            var procDt = request["procDt"] as String

            val refreshMyAssetList = myAssetService.refreshMyAssetList(this.dateUtils.convertYYYYMMDDToYYYYMM(procDt))

            var myAssetResponseDto = MyAssetResponseDto()
            myAssetResponseDto.createData(refreshMyAssetList)
            
            val exchageRateInfo = assetUtils.getExchangeRate()
            myAssetResponseDto.usdKrwRate = exchageRateInfo["USD"]!!
            myAssetResponseDto.jpyKrwRate = exchageRateInfo["JPY"]!!

            BaseResponseDto.success(myAssetResponseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }
    }
}