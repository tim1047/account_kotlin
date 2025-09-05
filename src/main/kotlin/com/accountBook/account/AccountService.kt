package com.accountbook.account

import com.accountbook.account.dto.AccountDto
import com.accountbook.account.dto.CreateAccountRequestDto
import com.accountbook.account.AccountRepository
import com.accountbook.model.Account
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    
    suspend fun getAccounts(strtDt: String, endDt: String, divisionId: String, memberId: String, categoryId: String, categorySeq: String, fixedPriceYn: String): List<AccountDto> {
        val accounts = accountRepository.getAccounts(strtDt, endDt, divisionId, memberId, categoryId, categorySeq, fixedPriceYn)
        return accounts
    }

    suspend fun insertAccount(createRequestDto : CreateAccountRequestDto) {
        var accountId = accountRepository.getAccountSeq()

        val accountEntity = Account(
            accountId=accountId,
            accountDt=createRequestDto.acoountDt,
            divisionId=createRequestDto.divisionId,
            paymentId=createRequestDto.paymentId,
            memberId=createRequestDto.memberId,
            categoryId=createRequestDto.categoryId,
            categorySeq=createRequestDto.categorySeq,
            price=createRequestDto.price,
            remark=createRequestDto.remark,
            impulseYn=createRequestDto.impulseYn,
            pointPrice=createRequestDto.pointPrice,
        ).apply { forceInsert = true }
        accountRepository.save(accountEntity)
    }
}