package com.accountbook.service

import com.accountbook.dto.MemberResponseDto
import com.accountbook.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    
    suspend fun getMembers(): List<MemberResponseDto> {
        return memberRepository.getMembers()
            .map { member ->
                MemberResponseDto(
                    memberId = member.memberId,
                    memberNm = member.memberNm,
                )
            }
    }
}