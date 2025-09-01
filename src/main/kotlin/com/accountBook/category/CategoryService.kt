package com.accountbook.category

import com.accountbook.category.dto.CategoryDto
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
}