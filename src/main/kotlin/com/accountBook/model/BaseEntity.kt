package com.accountbook.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

abstract class BaseEntity {
    
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var regDts: LocalDateTime? =  LocalDateTime.now()
    
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var modDts: LocalDateTime? =  LocalDateTime.now()
    
    var regpeId: String? = null
    var modpeId: String? = null
}