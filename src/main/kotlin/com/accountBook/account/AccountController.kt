package com.accountbook.account

import com.accountbook.model.Division
import com.accountbook.model.Account
import com.accountbook.dto.BaseResponseDto
import com.accountbook.account.dto.AccountResponseDto
import com.accountbook.account.dto.CreateAccountRequestDto
import com.accountbook.account.dto.UpdateAccountRequestDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
@RequestMapping("/account")
class AccountController (
    private val accountService: AccountService
) {

    @GetMapping("")
    suspend fun getAccounts(
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
        @RequestParam(value = "divisionId", required = false) divisionId: String = "",
        @RequestParam(value = "memberId", required = false) memberId: String = "",
        @RequestParam(value = "categoryId", required = false) categoryId: String = "",
        @RequestParam(value = "categorySeq", required = false) categorySeq: String = "",
        @RequestParam(value = "fixedPriceYn", required = false) fixedPriceYn: String = "",
    ): BaseResponseDto<List<AccountResponseDto>> {
        return try {
            val accounts = accountService.getAccounts(strtDt, endDt, divisionId, memberId, categoryId, categorySeq, fixedPriceYn)
            val responseDto = accounts.map {
                dto -> AccountResponseDto(
                    seq = dto.seq,
                    accountId = dto.accountId,
                    accountDt = dto.accountDt,
                    memberId = dto.memberId,
                    memberNm = dto.memberNm,
                    divisionId = dto.divisionId,
                    divisionNm = dto.divisionNm,
                    paymentId = dto.paymentId,
                    paymentNm = dto.paymentNm,
                    paymentType = dto.paymentType,
                    categoryId = dto.categoryId,
                    categoryNm = dto.categoryNm,
                    categorySeq = dto.categorySeq,
                    categorySeqNm = dto.categorySeqNm,
                    price = dto.price,
                    remark = dto.remark,
                    impulseYn = dto.impulseYn,
                    pointPrice = dto.pointPrice
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }           
    }

    @PostMapping("")
    suspend fun insertAccounts(
        @RequestBody request: CreateAccountRequestDto
    ): BaseResponseDto<String> {
        return try {
            accountService.insertAccount(request)
            BaseResponseDto.success("")
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }
    }

    @PutMapping("/{accountId}")
    suspend fun updateAccounts(
        @PathVariable accountId: String,
        @RequestBody updateAccountRequestDto: UpdateAccountRequestDto
    ): BaseResponseDto<String> {
        return try {
            accountService.updateAccount(accountId, updateAccountRequestDto)
            BaseResponseDto.success("")
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }
    }

    @DeleteMapping("/{accountId}")
    suspend fun deleteAccounts(
        @PathVariable accountId: String
    ): BaseResponseDto<String> {
        return try {
            accountService.deleteAccount(accountId)
            BaseResponseDto.success("")
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }
    }
}