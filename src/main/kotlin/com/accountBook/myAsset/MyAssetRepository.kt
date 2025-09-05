package com.accountbook.myAsset

import com.accountbook.model.MyAsset
import com.accountbook.myAsset.dto.MyAssetSumDto

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import AssetQuery

@Repository
interface MyAssetRepository : CoroutineCrudRepository<MyAsset, String> {
    
    @Query(MyAssetQuery.GET_MY_ASSET_SUM)
    suspend fun getMyAssetSum(procDt: String): List<MyAssetSumDto>
}