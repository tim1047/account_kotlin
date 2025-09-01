package com.accountbook.asset

import com.accountbook.dto.BaseResponseDto
import com.accountbook.asset.AssetService
import com.accountbook.asset.dto.AssetResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/asset")
class AssetController (
    private val assetService: AssetService
) {

    @GetMapping("")
    suspend fun getAssets(): BaseResponseDto<List<AssetResponseDto>> {
        return try {
            val assets = assetService.getAssets()
            val responseDto = assets.map {
                dto -> AssetResponseDto(
                    assetId = dto.assetId,
                    assetNm = dto.assetNm,
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }   
    }    
}