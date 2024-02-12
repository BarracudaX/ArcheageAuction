package com.arslan.archeage.service

import com.arslan.archeage.CategoryDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Category
import com.arslan.archeage.repository.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CategoryService(private val categoryRepository: CategoryRepository) {

    fun categories(archeageServer: ArcheageServer) : List<CategoryDTO> {
        val categories = categoryRepository.findAllByArcheageServer(archeageServer)

        val (topCategories,notTopCategories) = categories.partition { category -> category.parent == null }
        val topCategoriesMap = topCategories.map { category ->  CategoryDTO(category.id!!,category.name) }.associateBy { it.id!! }

        for(category in notTopCategories){
            var topParent = category.parent
            while (topParent?.parent != null){
                topParent = topParent.parent
            }
            topCategoriesMap[topParent!!.id!!]!!.addSubcategory(category)
        }

        return topCategoriesMap.values.toList()
    }


}