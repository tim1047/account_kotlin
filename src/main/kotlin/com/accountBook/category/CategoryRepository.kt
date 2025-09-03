package com.accountbook.category

import com.accountbook.model.Category
import com.accountbook.category.dto.CategoryDto
import com.accountbook.category.dto.CategorySumDto

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : CoroutineCrudRepository<Category, String> {
    
    @Query(CategoryQuery.GET_CATEGORYS)
    suspend fun getCategorys(): List<CategoryDto>

    @Query(CategoryQuery.GET_CATEGORY_SEQS_BY_CATEGORY_ID)
    suspend fun getCategorySeqs(categoryId: String): List<CategoryDto>

    @Query(CategoryQuery.GET_CATEGORYS_BY_DIVISION_ID)
    suspend fun getCategorysByDivisionId(@Param("divisionId") divisionId: String?,): List<CategoryDto>

    @Query(CategoryQuery.GET_CATEGORY_SUM)
    suspend fun getCategorySum(@Param("divisionId") divisionId: String?, @Param("strtDt") strtDt: String?, @Param("endDt") endDt: String?,): List<CategorySumDto>

    @Query(CategoryQuery.GET_CATEGORY_SEQ_SUM)
    suspend fun getCategorySeqSum(@Param("divisionId") divisionId: String?, @Param("strtDt") strtDt: String?, @Param("endDt") endDt: String?,): List<CategorySumDto>
}