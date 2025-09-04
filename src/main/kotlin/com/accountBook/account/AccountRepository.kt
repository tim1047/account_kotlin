package com.accountbook.account

import com.accountbook.model.Account
import com.accountbook.account.dto.AccountDto

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import AccountQuery

@Repository
interface AccountRepository : CoroutineCrudRepository<Account, String> {
    
    @Query(AccountQuery.GET_ACCOUNTS)
    suspend fun getAccounts(strtDt: String, endDt: String, divisionId: String, memberId: String, categoryId: String, categorySeq: String, fixedPriceYn: String): List<AccountDto>

    
}