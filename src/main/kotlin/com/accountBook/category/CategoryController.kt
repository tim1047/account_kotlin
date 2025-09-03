package com.accountbook.category

import com.accountbook.model.Category
import com.accountbook.dto.BaseResponseDto
import com.accountbook.category.dto.CategoryResponseDto
import com.accountbook.category.dto.CategorySeqResponseDto
import com.accountbook.category.dto.CategorySumResponseDto
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

    @GetMapping("/sum")
    suspend fun getCategorySum(
        @RequestParam(value = "divisionId", required = false) divisionId: String,
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
    ): BaseResponseDto<List<CategorySumResponseDto>> {
        return try {
            val categorySum = categoryService.getCategorySum(divisionId, strtDt, endDt)
            val responseDto = categorySum.map {
                dto -> CategorySumResponseDto(
                    categoryId = dto.categoryId,
                    categoryNm = dto.categoryNm,
                    divisionId = dto.divisionId,
                    categorySeq = null,
                    categorySeqNm = null,
                    fixedPriceYn = dto.fixedPriceYn,
                    sumPrice = dto.sumPrice,
                    totalSumPrice = dto.totalSumPrice,
                    data = dto.data
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }   
    }

    @GetMapping("/category-seq/sum")
    suspend fun getCategorySeqSum(
        @RequestParam(value = "divisionId", required = false) divisionId: String,
        @RequestParam(value = "strtDt", required = false) strtDt: String,
        @RequestParam(value = "endDt", required = false) endDt: String,
    ): BaseResponseDto<List<CategorySumResponseDto>> {
        return try {
            val categorySum = categoryService.getCategorySeqSum(divisionId, strtDt, endDt)
            val responseDto = categorySum.map {
                dto -> CategorySumResponseDto(
                    categoryId = dto.categoryId,
                    categoryNm = dto.categoryNm,
                    divisionId = dto.divisionId,
                    categorySeq = dto.categorySeq,
                    categorySeqNm = dto.categorySeqNm,
                    fixedPriceYn = dto.fixedPriceYn,
                    sumPrice = dto.sumPrice,
                    totalSumPrice = dto.totalSumPrice,
                    data = dto.data
                )
            }
            BaseResponseDto.success(responseDto)
        } catch (e: Exception) {
            BaseResponseDto.error(e.message)
        }   
    }
    
}