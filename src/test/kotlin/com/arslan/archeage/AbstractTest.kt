package com.arslan.archeage

import org.springframework.test.context.ActiveProfiles


abstract class AbstractTest {


    companion object{

        @JvmStatic
        fun continents() : Array<Continent> = Continent.values()

    }

}