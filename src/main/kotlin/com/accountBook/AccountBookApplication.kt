package com.accountbook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccountBookApplication

fun main(args: Array<String>) {
    runApplication<AccountBookApplication>(*args)
}