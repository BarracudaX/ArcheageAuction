package com.arslan.archeage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class ArcheageApplication

fun main(args: Array<String>) {
	runApplication<ArcheageApplication>(*args)
}
