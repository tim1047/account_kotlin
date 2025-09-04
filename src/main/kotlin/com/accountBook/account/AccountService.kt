package com.accountbook.account

import com.accountbook.account.dto.AccountDto
import com.accountbook.account.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    
    suspend fun getAccounts(strtDt: String, endDt: String, divisionId: String, memberId: String, categoryId: String, categorySeq: String, fixedPriceYn: String): List<AccountDto> {
        val accounts = accountRepository.getAccounts(strtDt, endDt, divisionId, memberId, categoryId, categorySeq, fixedPriceYn)
        return accounts
    }
}