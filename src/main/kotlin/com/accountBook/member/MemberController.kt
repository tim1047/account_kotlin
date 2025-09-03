package com.accountbook.member

import com.accountbook.model.Member
import com.accountbook.member.dto.MemberResponseDto
import com.accountbook.member.dto.MemberSumResponseDto
import com.accountbook.dto.BaseResponseDto
import com.accountbook.member.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

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

    @GetMapping("/sum")
    suspend fun getMembersSum(
        @RequestParam(value = "divisionId", required = false) divisionId: String,
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
    ): BaseResponseDto<List<MemberSumResponseDto>> {
        return try {
            val membersSum = memberService.getMembersSum(divisionId, strtDt, endDt)
            val responseDtp = membersSum.map {
                dto -> MemberSumResponseDto(
                    memberId = dto.memberId,
                    memberNm = dto.memberNm,
                    sumPrice = dto.sumPrice
                )
            }
            BaseResponseDto.success(responseDtp)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }   
    }
}