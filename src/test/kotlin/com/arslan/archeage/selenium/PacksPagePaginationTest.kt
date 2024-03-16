package com.arslan.archeage.selenium

import com.arslan.archeage.pageobjects.PacksPageObject
import com.arslan.archeage.pageobjects.component.ElementState
import com.arslan.archeage.pageobjects.component.PaginationButton
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PacksPagePaginationTest : AbstractPacksPageTest() {

    private lateinit var page: PacksPageObject

    @BeforeEach
    override fun setUp() {
        super.setUp()
        page = PacksPageObject(webDriver, port, packService, retryTemplate).get()
    }

    @Test
    fun `should have first and previous pagination buttons disabled when user is on the first page`() {
        page
            .selectServer(archeageServer)
            .pagination{ paginationData ->
                paginationData.firstBtn.elementState shouldBe ElementState.DISABLED
                paginationData.previousBtn.elementState shouldBe ElementState.DISABLED
            }
    }

    @Test
    fun `should have next and last pagination buttons disabled when user is on the last page`() {
        page
            .selectServer(archeageServer)
            .lastPage()
            .pagination{ paginationData ->
                paginationData.nextBtn.elementState shouldBe ElementState.DISABLED
                paginationData.lastBtn.elementState shouldBe ElementState.DISABLED
            }
    }

    @Test
    fun `should have pagination for next pages`() {
        page
            .selectServer(archeageServer)
            .pagination { paginationData ->
                paginationData.firstBtn.elementState shouldBe ElementState.DISABLED
                paginationData.previousBtn.elementState shouldBe ElementState.DISABLED
                paginationData.nextBtn.elementState shouldBe ElementState.ENABLED
                paginationData.lastBtn.elementState shouldBe ElementState.ENABLED
            }
    }


    @Test
    fun `should have pagination for previous page`() {
        page
            .selectServer(archeageServer)
            .pagination { paginationData ->
                paginationData.nextBtn.elementState shouldBe ElementState.ENABLED
                paginationData.previousBtn.elementState shouldBe ElementState.DISABLED
            }
            .nextPage()
            .pagination { paginationData ->
                paginationData.previousBtn.elementState shouldBe ElementState.ENABLED
            }
    }

    @Test
    fun `should have pagination for last page`() {
        page
            .selectServer(archeageServer)
            .pagination { paginationData ->
                paginationData.lastBtn.elementState shouldBe ElementState.ENABLED
                paginationData.firstBtn.elementState shouldBe ElementState.DISABLED
            }
            .lastPage()
            .pagination { paginationData ->
                paginationData.lastBtn.elementState shouldBe ElementState.DISABLED
                paginationData.firstBtn.elementState shouldBe ElementState.ENABLED
            }
    }

    @Test
    fun `should have the selected page active`() {
        page
            .selectServer(archeageServer)
            .pagination { paginationData ->
                paginationData.paginationNums.shouldContain(PaginationButton("1", ElementState.ENABLED,true))
                paginationData.paginationNums.shouldContain(PaginationButton("2", ElementState.ENABLED,false))
            }.selectPage(2)
            .pagination { paginationData ->
                paginationData.paginationNums.shouldContain(PaginationButton("1", ElementState.ENABLED,false))
                paginationData.paginationNums.shouldContain(PaginationButton("2", ElementState.ENABLED,true))
            }
    }



}