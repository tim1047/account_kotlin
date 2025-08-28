package com.accountbook.controller

import com.accountbook.model.Member
import com.accountbook.service.MemberService
import com.accountbook.dto.MemberResponseDto
import com.accountbook.dto.BaseResponseDto
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
            BaseResponseDto.success(members)
        } catch (e: Exception) {
            BaseResponseDto.error("FAIL")
        }   
    }
}