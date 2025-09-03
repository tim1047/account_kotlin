package com.accountbook.member

import com.accountbook.model.Member
import com.accountbook.member.dto.MemberSumDto
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import MemberQuery

@Repository
interface MemberRepository : CoroutineCrudRepository<Member, String> {
    
    @Query(MemberQuery.GET_MEMBERS)
    suspend fun getMembers(): List<Member>

    @Query(MemberQuery.GET_MEMBERS_SUM)
    suspend fun getMembersSum(divisionId: String, strtDt: String, endDt: String): List<MemberSumDto>
}