package com.accountbook.myAsset

import com.accountbook.model.MyAssetAccum

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import org.springframework.data.r2dbc.repository.Query
import AssetQuery

@Repository
interface MyAssetAccumRepository : CoroutineCrudRepository<MyAssetAccum, String> {

    suspend fun deleteByAccumDt(accumDt: String)

    suspend fun deleteByAccumDtAndMyAssetId(accumDt: String, myAssetId: String)

    @Query(MyAssetQuery.UPDATE_MY_ASSET_ACCUM)
    suspend fun updateMyAssetAcuumByPrimaryKey(entity: MyAssetAccum)
}