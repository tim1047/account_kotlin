package com.accountbook.division

import com.accountbook.division.dto.DivisionDto
import com.accountbook.division.dto.DivisionSumDto
import com.accountbook.division.dto.DivisionSumGroupByMonthDto
import com.accountbook.division.DivisionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter
import java.time.YearMonth
import DivisionConstants
import kotlin.math.round

@Service
class DivisionService(
    private val divisionRepository: DivisionRepository
) {
    
    suspend fun getDivisions(): List<DivisionDto> {
        return divisionRepository.getDivisions()
            .map { division ->
                DivisionDto(
                    divisionId = division.divisionId,
                    divisionNm = division.divisionNm,
                    sumPrice = null
                )
            }
    }

    suspend fun getDivisionsSum(strtDt: String, endDt: String): DivisionSumDto {
        val divisionSums = divisionRepository.getDivisionsSum(strtDt, endDt)
        var income = BigDecimal.ZERO
        var expense = BigDecimal.ZERO
        var invest = BigDecimal.ZERO
        var investRate = ""

        for (divisionSum in divisionSums) {
            if (DivisionConstants.INCOME.equals(divisionSum.divisionId)) {
                income = divisionSum.sumPrice
            } else if (DivisionConstants.INVEST.equals(divisionSum.divisionId)) {
                invest = divisionSum.sumPrice
            } else {
                expense = divisionSum.sumPrice
            }
        }
        val interest = income.subtract(expense)

        if (income > BigDecimal.ZERO) {
            investRate = invest.multiply(BigDecimal(100)).divide(income, 1, RoundingMode.HALF_UP).toString() + "%"
        }
        return DivisionSumDto(income, interest, expense, invest, investRate)
    }

    suspend fun getDivisionSumGroupByMonth(divisionId: String, procDt: String): List<DivisionSumGroupByMonthDto> {
        var divisionSumGroupByMonthList = ArrayList<DivisionSumGroupByMonthDto>();

        val inputFormatter = DateTimeFormatter.ofPattern("yyyyMM")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

        val yearMonth = YearMonth.parse(procDt, inputFormatter)

        for (i in 0..5) {
            val target = yearMonth.minusMonths(i.toLong())
            val firstDay = target.atDay(1).format(outputFormatter)
            val lastDay = target.atEndOfMonth().format(outputFormatter)

            val divisionSums = divisionRepository.getDivisionsSum(firstDay, lastDay)

            for (divisionSum in divisionSums) {
                if (divisionId.equals(divisionSum.divisionId)) {
                    divisionSumGroupByMonthList.add(DivisionSumGroupByMonthDto(divisionSum.divisionId, divisionSum.divisionNm, divisionSum.sumPrice, target.monthValue))
                }
            }
        }
        return divisionSumGroupByMonthList.reversed()
    }
}