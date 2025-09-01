package com.accountbook.division

import com.accountbook.division.dto.DivisionDto
import com.accountbook.division.DivisionRepository
import org.springframework.stereotype.Service

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
                )
            }
    }
}