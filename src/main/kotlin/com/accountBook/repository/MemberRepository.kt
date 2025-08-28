package com.accountbook.repository

import com.accountbook.model.Member
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberRepository : CoroutineCrudRepository<Member, String> {
    
    @Query("SELECT * FROM account_member")
    suspend fun getMembers(): List<Member>
}