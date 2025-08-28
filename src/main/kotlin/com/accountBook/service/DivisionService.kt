package com.accountbook.service

import com.accountbook.dto.DivisionResponseDto
import com.accountbook.repository.DivisionRepository
import org.springframework.stereotype.Service

@Service
class DivisionService(
    private val divisionRepository: DivisionRepository
) {
    
    suspend fun getDivisions(): List<DivisionResponseDto> {
        return divisionRepository.getDivisions()
            .map { division ->
                DivisionResponseDto(
                    divisionId = division.divisionId,
                    divisionNm = division.divisionNm,
                )
            }
    }
}