package com.accountbook.controller

import com.accountbook.model.Division
import com.accountbook.dto.DivisionResponseDto
import com.accountbook.dto.BaseResponseDto
import com.accountbook.service.DivisionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/division")
class DivisionController (
    private val divisionService: DivisionService
) {

    @GetMapping("")
    suspend fun getDivisions(): BaseResponseDto<List<DivisionResponseDto>> {
        return try {
            val divisions = divisionService.getDivisions()
            BaseResponseDto.success(divisions)
        } catch (e: Exception) {
            BaseResponseDto.error("FAIL")
        }           
    }
}