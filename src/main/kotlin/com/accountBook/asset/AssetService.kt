package com.accountbook.asset

import com.accountbook.asset.dto.AssetDto
import com.accountbook.asset.AssetRepository
import org.springframework.stereotype.Service

@Service
class AssetService(
    private val assetRepository: AssetRepository
) {
    
    suspend fun getAssets(): List<AssetDto> {
        return assetRepository.getAssets()
            .map { asset ->
                AssetDto(
                    assetId = asset.assetId,
                    assetNm = asset.assetNm,
                )
            }
    }
}