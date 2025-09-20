
package com.accountbook.utils

import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.YearMonth

@Service
class DateUtils (
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd"),
    private val monthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM")
) {
    suspend fun getCurrentDt(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
    }

    suspend fun getCurrentYearMonth(date: LocalDate): YearMonth {
        return YearMonth.from(date)
    }

    suspend fun getCurentYearMonthByYYYYMM(dt: String): YearMonth {
        return YearMonth.parse(dt, this.monthFormatter)
    }

    suspend fun convertYYYYMMDDToYYYYMM(dt: String): String {
        val localDate = LocalDate.parse(dt, this.dateFormatter)
        return this.getCurrentYearMonth(localDate).format(this.monthFormatter)
    }

    suspend fun convertDtToDate(dt: String): LocalDate {
        return LocalDate.parse(dt, this.dateFormatter)
    }

    suspend fun convertYearMonthToDt(yearMonth: YearMonth): String {
        return yearMonth.format(this.monthFormatter)
    }

    suspend fun getFirstDay(yearMonth: YearMonth): String {
        return yearMonth.atDay(1).format(this.dateFormatter)
    }

    suspend fun getLastDay(yearMonth: YearMonth): String {
        return yearMonth.atEndOfMonth().format(this.dateFormatter)
    }

    suspend fun getYear(dt: String): Int {
        return this.getCurentYearMonthByYYYYMM(dt).year
    }

    suspend fun getMonth(dt: String): Int {
        return this.getCurentYearMonthByYYYYMM(dt).monthValue
    }
}