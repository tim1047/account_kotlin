package com.accountbook.category

import com.accountbook.model.Category
import com.accountbook.category.dto.CategoryDto

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : CoroutineCrudRepository<Category, String> {
    
    @Query("SELECT * FROM category")
    suspend fun getCategorys(): List<CategoryDto>

    @Query("SELECT category_id, category_seq, category_seq_nm FROM category_dtl WHERE 1=1 AND category_id = :categoryId")
    suspend fun getCategorySeqs(categoryId: String): List<CategoryDto>

    @Query("""
        SELECT c.category_id
            ,  c.category_nm
            ,  d.division_id
            ,  d.division_nm
        FROM division_category_mpng AS dcm
        INNER JOIN category AS c
        ON dcm.category_id = c.category_id
        INNER JOIN division AS d
        ON dcm.division_id = d.division_id
        WHERE 1=1
        AND   dcm.division_id = :divisionId
    """)
    suspend fun getCategorysByDivisionId(@Param("divisionId") divisionId: String?,): List<CategoryDto>
}