package com.accountbook.division

import com.accountbook.division.dto.DivisionDto
import com.accountbook.division.dto.DivisionSumDto
import com.accountbook.division.dto.DivisionSumGroupByMonthDto
import com.accountbook.division.DivisionRepository
import com.accountbook.utils.DateUtils
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.format.DateTimeFormatter
import java.time.YearMonth
import java.time.LocalDate
import DivisionConstants
import kotlin.math.round
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableListOf

@Service
class DivisionService(
    private val divisionRepository: DivisionRepository,
    private val dateUtils: DateUtils
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
        var divisionSumGroupByMonthList = ArrayList<DivisionSumGroupByMonthDto>()

        val yearMonth = this.dateUtils.getCurentYearMonthByYYYYMM(procDt)

        for (i in 0..5) {
            val target = yearMonth.minusMonths(i.toLong())
            val firstDay = this.dateUtils.getFirstDay(yearMonth)
            val lastDay = this.dateUtils.getLastDay(yearMonth)

            val divisionSums = divisionRepository.getDivisionsSum(firstDay, lastDay)

            for (divisionSum in divisionSums) {
                if (divisionId.equals(divisionSum.divisionId)) {
                    divisionSumGroupByMonthList.add(DivisionSumGroupByMonthDto(divisionSum.divisionId, divisionSum.divisionNm, divisionSum.sumPrice, target.monthValue))
                }
            }
        }
        return divisionSumGroupByMonthList.reversed()
    }

    suspend fun getDivisionSumDaily(divisionId: String, strtDt: String, endDt: String): Any {
        var divisionSumDaily = divisionRepository.getDivisionsSumDaily(divisionId, strtDt, endDt)

        var divisionSumDailyList = ArrayList<Any>()
        var divisionSumDailyMap: MutableMap<String, MutableList<BigDecimal>> = mutableMapOf()
        var accountYYYYMMSet: MutableSet<String> = mutableSetOf("일자")

        for (d in divisionSumDaily) {
            val year = this.dateUtils.getYear(d.accountYYYYMM)
            val month = this.dateUtils.getMonth(d.accountYYYYMM)

            accountYYYYMMSet.add(year.toString() + "년 " + month.toString() + "월")
            
            if (divisionSumDailyMap.containsKey(month.toString())) {
                val lastSumPirce = divisionSumDailyMap[month.toString()]!!.last()
                divisionSumDailyMap[month.toString()]!!.add(lastSumPirce.add(d.sumPrice!!))
            } else {
                divisionSumDailyMap[month.toString()] = mutableListOf(d.sumPrice!!)
            }
        }
        divisionSumDailyList.add(0, accountYYYYMMSet.toMutableList())

        for (i in 0..30) {
            var resultList: MutableList<Any> = mutableListOf()
            resultList.add(0, (i+1).toString() + "일")

            divisionSumDailyMap.forEach { (_, value) ->
                resultList.add(value.getOrElse(i) { value.last() })
            }
            divisionSumDailyList.add(resultList)
        }
        return divisionSumDailyList
    }
}