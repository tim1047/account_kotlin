package com.accountbook.repository

import com.accountbook.model.Division
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DivisionRepository : CoroutineCrudRepository<Division, String> {
    
    @Query("SELECT * FROM division")
    suspend fun getDivisions(): List<Division>
}