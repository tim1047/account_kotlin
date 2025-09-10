package com.accountbook.account

import com.accountbook.account.dto.AccountDto
import com.accountbook.account.dto.CreateAccountRequestDto
import com.accountbook.account.dto.UpdateAccountRequestDto
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
            accountDt=createRequestDto.accountDt,
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

    suspend fun updateAccount(accountId: String, updateAccountRequestDto: UpdateAccountRequestDto) {
        val existingAccount = accountRepository.findById(accountId)
            ?: throw Exception("Account not found: $accountId")
        
        val updatedAccount = existingAccount.copy(
            accountDt = updateAccountRequestDto.accountDt ?: existingAccount.accountDt,
            divisionId = updateAccountRequestDto.divisionId ?: existingAccount.divisionId,
            memberId = updateAccountRequestDto.memberId ?: existingAccount.memberId,
            paymentId = updateAccountRequestDto.paymentId ?: existingAccount.paymentId,
            categoryId = updateAccountRequestDto.categoryId ?: existingAccount.categoryId,
            categorySeq = updateAccountRequestDto.categorySeq ?: existingAccount.categorySeq,
            price = updateAccountRequestDto.price ?: existingAccount.price,
            remark = updateAccountRequestDto.remark ?: existingAccount.remark,
            impulseYn = updateAccountRequestDto.impulseYn ?: existingAccount.impulseYn,
            pointPrice = updateAccountRequestDto.pointPrice ?: existingAccount.pointPrice
        )
        accountRepository.save(updatedAccount)
    }

    suspend fun deleteAccount(accountId: String) {
        accountRepository.deleteById(accountId)
    }
}