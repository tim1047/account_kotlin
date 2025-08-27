package com.accountbook.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/division")
class DivisionController {

    @GetMapping("")
    fun division(): String {
        return "division"
    }
}