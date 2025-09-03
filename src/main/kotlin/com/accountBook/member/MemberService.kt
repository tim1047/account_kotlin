package com.accountbook.member

import com.accountbook.member.dto.MemberDto
import com.accountbook.member.dto.MemberSumDto
import com.accountbook.member.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    
    suspend fun getMembers(): List<MemberDto> {
        return memberRepository.getMembers()
            .map { member ->
                MemberDto(
                    memberId = member.memberId,
                    memberNm = member.memberNm,
                )
            }
    }

    suspend fun getMembersSum(divisionId: String, strtDt: String, endDt: String): List<MemberSumDto> {
        return memberRepository.getMembersSum(divisionId, strtDt, endDt)
    }
}