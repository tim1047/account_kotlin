package com.accountbook.division

import com.accountbook.model.Division
import com.accountbook.division.dto.DivisionDto
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import DivisionQuery

@Repository
interface DivisionRepository : CoroutineCrudRepository<Division, String> {
    
    @Query(DivisionQuery.GET_DIVISIONS)
    suspend fun getDivisions(): List<Division>

    @Query(DivisionQuery.GET_DIVISIONS_SUM)
    suspend fun getDivisionsSum(strtDt: String, endDt: String): List<DivisionDto>
}