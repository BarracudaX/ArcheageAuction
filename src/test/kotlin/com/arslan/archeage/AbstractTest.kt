package com.arslan.archeage

import com.arslan.archeage.entity.Price
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import kotlin.random.Random


abstract class AbstractTest {

    companion object{

        @JvmStatic
        fun continents() : Array<Continent> = Continent.values()

        @JvmStatic
        fun contentTypesOtherThanHTML() : Array<MediaType> = arrayOf(MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_ATOM_XML,MediaType.APPLICATION_JSON)

        @JvmStatic
        fun contentTypesOtherThanJson() : Array<MediaType> = arrayOf(MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_ATOM_XML,MediaType.TEXT_HTML)

        @JvmStatic
        fun httpMethodsOtherThanGet() : List<HttpMethod> = HttpMethod.values().filter { it !in arrayOf(HttpMethod.GET, HttpMethod.OPTIONS,HttpMethod.TRACE,HttpMethod.HEAD) }

        @JvmStatic
        fun invalidPasswords() : Array<String> = arrayOf("shortP1","TooLongPassword12","12349512","awasqweaswq")

        @JvmStatic
        fun invalidEmails() : Array<String> = arrayOf(
            "userexample.com","user@.com","user@example.","user@exam@ple.com","user@ example.com",
            "user@example. com","user@.example.com","user@example.com."
        )

        @JvmStatic
        fun blankStrings() : Array<String> = arrayOf("      ","","   ")

        @JvmStatic
        fun invalidContinentValues() : Array<String> = arrayOf("wst","est","NR","WE","EST","","   ")
    }


    protected fun randomPrice() : Price = Price(Random.nextInt(0,100), Random.nextInt(0,99), Random.nextInt(0,99))

}