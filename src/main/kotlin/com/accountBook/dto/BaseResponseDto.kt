package com.accountbook.dto

data class BaseResponseDto<T>(
    val resultMessage: String?,
    val resultData: T?
) {
    companion object {
        // 성공 응답
        fun <T> success(data: T): BaseResponseDto<T> {
            return BaseResponseDto(
                resultMessage = "SUCCESS",
                resultData = data
            )
        }
        
        // 성공 응답 (메시지 커스텀)
        fun <T> success(data: T, message: String): BaseResponseDto<T> {
            return BaseResponseDto(
                resultMessage = message,
                resultData = data
            )
        }
        
        // 실패 응답
        fun <T> error(message: String? = null): BaseResponseDto<T> {
            return BaseResponseDto(
                resultMessage = message,
                resultData = null
            )
        }
    }
}