package com.accountbook.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HelloController {

    @GetMapping("/hello")
    fun hello(): String {
        return "Hello Account Book API! 🚀"
    }

    @GetMapping("/status")
    fun status(): Map<String, String> {
        return mapOf(
            "status" to "OK",
            "service" to "Account Book API",
            "version" to "1.0.0"
        )
    }
}