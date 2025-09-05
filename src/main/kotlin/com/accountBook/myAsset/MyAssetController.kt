package com.accountbook.myAsset

import com.accountbook.dto.BaseResponseDto
import com.accountbook.myAsset.dto.MyAssetSumResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.YearMonth

@RestController
@RequestMapping("/my-asset")
class MyAssetController (
    private val myAssetService: MyAssetService
) {

    @GetMapping("/sum")
    suspend fun getMysAssetSum(
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
    ): BaseResponseDto<List<MyAssetSumResponseDto>> {
        return try {
            var resultList = ArrayList<MyAssetSumResponseDto>();
            
            val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
            val monthFormatter = DateTimeFormatter.ofPattern("yyyyMM")

            val startDate = LocalDate.parse(strtDt, dateFormatter)
            val endDate = LocalDate.parse(endDt, dateFormatter)

            var currentMonth = YearMonth.from(startDate)
            val endMonth = YearMonth.from(endDate)

            while (!currentMonth.isAfter(endMonth)) {
                var procDt = currentMonth.format(monthFormatter)

                val myAssetSums = myAssetService.getMyAssetSum(procDt)

                resultList.add(MyAssetSumResponseDto(procDt, myAssetSums, myAssetSums.get(0).sumPrice))
                currentMonth = currentMonth.plusMonths(1)
            }
            BaseResponseDto.success(resultList)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }   
    }    
}