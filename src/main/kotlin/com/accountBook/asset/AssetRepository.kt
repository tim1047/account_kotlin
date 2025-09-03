package com.accountbook.asset

import com.accountbook.model.Asset
import com.accountbook.asset.dto.AssetDto

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import AssetQuery

@Repository
interface AssetRepository : CoroutineCrudRepository<Asset, String> {
    
    @Query(AssetQuery.GET_ASSETS)
    suspend fun getAssets(): List<AssetDto>
}