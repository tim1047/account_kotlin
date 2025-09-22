package com.accountbook.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime
import java.time.ZoneId

abstract class BaseEntity<T>: Persistable<T> {
    
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var regDts: LocalDateTime? =  LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var modDts: LocalDateTime? =  LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    
    var regpeId: String? = "SKW"
    var modpeId: String? = "SKW"

    @Transient
    var forceInsert: Boolean = false
}