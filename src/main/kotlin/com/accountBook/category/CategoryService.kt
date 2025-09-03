package com.accountbook.category

import com.accountbook.category.dto.CategoryDto
import com.accountbook.category.dto.CategorySumDto
import com.accountbook.category.CategoryRepository
import org.springframework.stereotype.Service

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {
    
    suspend fun getCategorys(divisionId: String? = null): List<CategoryDto> {
        val cateogryDtoList = when {
            divisionId != null -> {
                categoryRepository.getCategorysByDivisionId(divisionId)
            }
            else -> {
                categoryRepository.getCategorys()
            }
        }
        return cateogryDtoList
    }

    suspend fun getCategorySeqs(categoryId: String): List<CategoryDto> {
        val cateogryDtoList = categoryRepository.getCategorySeqs(categoryId)
        return cateogryDtoList
    }

    suspend fun getCategorySum(divisionId: String, strtDt: String, endDt: String): List<CategorySumDto> {
        val categorySumList = categoryRepository.getCategorySum(divisionId, strtDt, endDt)

        val categorySumMap = mutableMapOf<String, CategorySumDto>()
        for (categorySum in categorySumList) {
            val categoryId = categorySum.categoryId
            if (categorySumMap.containsKey(categoryId)) {
                if (categorySumMap[categoryId] != null) {
                    categorySumMap[categoryId]!!.sumPrice.add(categorySum.sumPrice)
                    categorySumMap[categoryId]!!.createData(categorySum.categorySeq!!, categorySum.categorySeqNm!!, categorySum.sumPrice)
                }
            } else {
                categorySum.createData(categorySum.categorySeq!!, categorySum.categorySeqNm!!, categorySum.sumPrice)
                categorySumMap[categoryId] = categorySum
            }
        }
        return categorySumMap.values.toList()
    }

    suspend fun getCategorySeqSum(divisionId: String, strtDt: String, endDt: String): List<CategorySumDto> {
        val categorySeqSumList = categoryRepository.getCategorySeqSum(divisionId, strtDt, endDt)
        return categorySeqSumList
    }
}