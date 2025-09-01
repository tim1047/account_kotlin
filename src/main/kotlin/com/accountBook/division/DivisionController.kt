package com.accountbook.division

import com.accountbook.model.Division
import com.accountbook.division.dto.DivisionResponseDto
import com.accountbook.dto.BaseResponseDto
import com.accountbook.division.DivisionService
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
            val responseDto = divisions.map {
                dto -> DivisionResponseDto(
                    divisionId = dto.divisionId,
                    divisionNm = dto.divisionNm
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error("FAIL")
        }           
    }
}