
package com.accountbook.utils

import org.springframework.stereotype.Service
import java.time.format.DateTimeFormatter
import java.time.LocalDate


@Service
class DateUtils (

) {
    suspend fun getCurrentDt(): String {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
    }
}