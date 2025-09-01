package com.accountbook.member

import com.accountbook.model.Member
import com.accountbook.member.dto.MemberResponseDto
import com.accountbook.dto.BaseResponseDto
import com.accountbook.member.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/member")
class MemberController (
    private val memberService: MemberService
) {

    @GetMapping("")
    suspend fun getMembers(): BaseResponseDto<List<MemberResponseDto>> {
        return try {
            val members = memberService.getMembers()
            val responseDtp = members.map {
                dto -> MemberResponseDto(
                    memberId = dto.memberId,
                    memberNm = dto.memberNm
                )
            }
            BaseResponseDto.success(responseDtp)
        } catch (e: Exception) {
            BaseResponseDto.error("FAIL")
        }   
    }
}