package com.arslan.archeage.service

import com.arslan.archeage.CategoryDTO
import com.arslan.archeage.entity.ArcheageServer
import com.arslan.archeage.entity.Category
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CategoryServiceITest(private val categoryService: CategoryService) : AbstractITest() {

    private lateinit var archeageServer: ArcheageServer
    private lateinit var anotherArcheageServer: ArcheageServer

    @BeforeEach
    fun setUp() {
        archeageServer = archeageServerRepository.save(ArcheageServer("ANY_ARCHEAGE_SERVER_1"))
        anotherArcheageServer = archeageServerRepository.save(ArcheageServer("ANY_ARCHEAGE_SERVER_2"))
    }

    @Test
    fun `should return empty list when no categories exist`() {
        categoryService.categories(archeageServer).shouldBeEmpty()
    }

    @Test
    fun `should return empty list when requesting categories of archeage server that does not have any`() {
        categoryRepository.save(Category("ANY",null,archeageServer))

        categoryService.categories(anotherArcheageServer).shouldBeEmpty()
    }

    @Test
    fun `should return top categories with subcategories properly set`() {
        val topOneCategory = categoryRepository.save(Category("TOP_CATEGORY_1",null,archeageServer))
        val topTwoCategory = categoryRepository.save(Category("TOP_CATEGORY_2",null,archeageServer))
        val notTopCategory = categoryRepository.save(Category("NOT_TOP_CATEGORY_1",topOneCategory,archeageServer))
        val notTopCategory2 = categoryRepository.save(Category("NOT_TOP_CATEGORY_1",topTwoCategory,archeageServer))
        val notTopCategory3 = categoryRepository.save(Category("NOT_TOP_CATEGORY_3",notTopCategory,archeageServer))

        val expectedCategories = listOf(
            CategoryDTO(
                topOneCategory.id!!,
                topOneCategory.name,
                mutableListOf(CategoryDTO(notTopCategory.id!!,notTopCategory.name,mutableListOf(CategoryDTO(notTopCategory3.id!!,notTopCategory3.name))))
            ),
            CategoryDTO(topTwoCategory.id!!,topTwoCategory.name,mutableListOf(CategoryDTO(notTopCategory2.id!!,notTopCategory2.name)))
        )

        categoryService.categories(archeageServer).shouldContainExactlyInAnyOrder(expectedCategories)
    }
}