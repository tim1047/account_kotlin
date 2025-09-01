package com.accountbook.category

import com.accountbook.model.Category
import com.accountbook.dto.BaseResponseDto
import com.accountbook.category.dto.CategoryResponseDto
import com.accountbook.category.dto.CategorySeqResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PathVariable

@RestController
@RequestMapping("/category")
class CategoryController (
    private val categoryService: CategoryService
) {

    @GetMapping("")
    suspend fun getCategorys(
         @RequestParam(value = "divisionId", required = false) divisionId: String?,
    ): BaseResponseDto<List<CategoryResponseDto>> {
        return try {
            val categorys = categoryService.getCategorys(divisionId)
            val responseDto = categorys.map {
                dto -> CategoryResponseDto(
                    categoryId = dto.categoryId,
                    categoryNm = dto.categoryNm,
                    divisionId = dto.divisionId,
                    divisionNm = dto.divisionNm
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }   
    }

    @GetMapping("/{categoryId}/category-seq")
    suspend fun getCategorySeqs(
        @PathVariable categoryId: String
    ): BaseResponseDto<List<CategorySeqResponseDto>> {
        return try {
            val categorySeqs = categoryService.getCategorySeqs(categoryId)
            val responseDto = categorySeqs.map {
                dto -> CategorySeqResponseDto(
                    categoryId = dto.categoryId,
                    categoryNm = dto.categoryNm,
                    categorySeq = dto.categorySeq!!,
                    categorySeqNm = dto.categorySeqNm!!
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }   
    }
    
}