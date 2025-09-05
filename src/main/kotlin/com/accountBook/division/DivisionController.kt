package com.accountbook.division

import com.accountbook.model.Division
import com.accountbook.division.dto.DivisionResponseDto
import com.accountbook.division.dto.DivisionSumResponseDto
import com.accountbook.division.dto.DivisionSumGroupByMonthResponseDto
import com.accountbook.dto.BaseResponseDto
import com.accountbook.division.DivisionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/division")
class DivisionController (
    private val divisionService: DivisionService
) {

    @GetMapping("")
    suspend fun getDivisions(): BaseResponseDto<List<DivisionResponseDto>> {
        return try {
            val divisions = divisionService.getDivisions()
            val responseDto = divisions.map {
                dto -> DivisionResponseDto(
                    divisionId = dto.divisionId,
                    divisionNm = dto.divisionNm
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }           
    }

    @GetMapping("/sum")
    suspend fun getDivisionsSum(
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
    ): BaseResponseDto<DivisionSumResponseDto> {
        return try {
            val divisionSum = divisionService.getDivisionsSum(strtDt, endDt)
            BaseResponseDto.success(DivisionSumResponseDto(divisionSum.income, divisionSum.interest, divisionSum.expense, divisionSum.invest, divisionSum.investRate))
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }           
    }

    @GetMapping("/{divisionId}/sum-group-by-month")
    suspend fun getDivisionSumGroupByMonth(
        @PathVariable(value = "divisionId", required = false) divisionId: String,
        @RequestParam(value = "procDt", required = false) procDt: String,
    ): BaseResponseDto<DivisionSumGroupByMonthResponseDto> {
        return try {
            val divisionSumGroupByMonthList = divisionService.getDivisionSumGroupByMonth(divisionId, procDt)
            BaseResponseDto.success(DivisionSumGroupByMonthResponseDto(divisionSumGroupByMonthList))
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }           
    }

    @GetMapping("/{divisionId}/sum-daily")
    suspend fun getDivisionSumDaily(
        @PathVariable(value = "divisionId", required = false) divisionId: String,
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
    ): BaseResponseDto<Any> {
        return try {
            val divisionSumDaily = divisionService.getDivisionSumDaily(divisionId, strtDt, endDt)
            BaseResponseDto.success(divisionSumDaily)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }           
    }
}