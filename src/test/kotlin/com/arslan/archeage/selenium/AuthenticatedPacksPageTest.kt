package com.arslan.archeage.selenium

import com.arslan.archeage.entity.pack.PackProfitKey
import com.arslan.archeage.pageobjects.AuthenticatedPacksPageObject
import com.arslan.archeage.pageobjects.AuthenticationData
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthenticatedPacksPageTest : AbstractPacksPageTest(){

    private lateinit var page: AuthenticatedPacksPageObject

    @BeforeEach
    override fun setUp() {
        super.setUp()
        page = AuthenticatedPacksPageObject(AuthenticationData(user.email,userPassword,user.id!!),webDriver,port,packService,retryTemplate).get() as AuthenticatedPacksPageObject
    }

    @Test
    fun `should allow user to update pack percentage`() {
        var packID = -1L
        page
            .selectServer(archeageServer)
            .packs { packs -> packID = packs.first().id }
        val pack = packRepository.findById(packID).get()
        packProfitRepository.findById(PackProfitKey(pack,user)).get().percentage shouldBe 100

        page.changePercentage(packID,110)

        packProfitRepository.findById(PackProfitKey(pack,user)).get().percentage shouldBe 110
    }

}