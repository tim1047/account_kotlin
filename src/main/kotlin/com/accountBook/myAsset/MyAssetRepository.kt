package com.accountbook.myAsset

import com.accountbook.model.MyAsset
import com.accountbook.myAsset.dto.MyAssetSumDto
import com.accountbook.myAsset.dto.MyAssetDto

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import AssetQuery

@Repository
interface MyAssetRepository : CoroutineCrudRepository<MyAsset, String> {

    @Query(MyAssetQuery.GET_MY_ASSET_SEQ)
    suspend fun getMyAssetSeq(): Long

    @Query(MyAssetQuery.GET_MY_ASSET_ACCUM_LIST)
    suspend fun getMyAssetAccumList(procDt: String): List<MyAssetDto>

    @Query(MyAssetQuery.GET_MY_ASSET_SUM)
    suspend fun getMyAssetSum(procDt: String): List<MyAssetSumDto>

    @Query(MyAssetQuery.GET_MY_ASSET_LIST)
    suspend fun getMyAssetList(myAssetId: String): List<MyAssetDto>
}