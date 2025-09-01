package com.accountbook.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

abstract class BaseDto {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var regDts: LocalDateTime? =  LocalDateTime.now()
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var modDts: LocalDateTime? =  LocalDateTime.now()

    var regpeId: String? = null
    var modpeId: String? = null
}