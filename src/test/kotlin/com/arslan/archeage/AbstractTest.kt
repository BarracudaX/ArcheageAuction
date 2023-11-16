package com.arslan.archeage

import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.MimeTypeUtils


abstract class AbstractTest {


    companion object{

        @JvmStatic
        fun continents() : Array<Continent> = Continent.values()

        @JvmStatic
        fun contentTypesOtherThanHTML() : Array<MediaType> = arrayOf(MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_ATOM_XML,MediaType.APPLICATION_JSON)

        @JvmStatic
        fun invalidPasswords() : Array<String> = arrayOf("shortP1","TooLongPassword12","12349512","awasqweaswq")

        @JvmStatic
        fun invalidEmails() : Array<String> = arrayOf(
            "userexample.com","user@.com","user@example.","user@exam@ple.com","user@ example.com",
            "user@example. com","user@.example.com","user@example.com."
        )
    }

}