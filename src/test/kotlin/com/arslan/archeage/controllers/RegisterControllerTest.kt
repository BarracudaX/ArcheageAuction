package com.arslan.archeage.controllers

import io.mockk.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

class RegisterControllerTest(private val mockMvc: MockMvc) : AbstractControllerTest() {

    private val validRegistrationForm = RegistrationForm("valid@email.com","ValidPass1","ValidPass1")

    @WithAnonymousUser
    @Test
    fun `should return register view if registration form contains not equal passwords without performing registration`() {
        mockMvc.post("/register"){
                contentType = MediaType.APPLICATION_FORM_URLENCODED
                param("email","test@email.com")
                param("password","ValidPass123")
                param("repeatedPassword","NOT_EQUAL_PASSWORD")
                with(csrf())
            }.andExpect {
                status {
                    isOk()
                    view { name("register") }
                    model {
                        hasErrors()
                        errorCount(1)
                    }
                }
            }
        verifyAll { userServiceMock wasNot called }
    }

    @MethodSource("invalidPasswords")
    @ParameterizedTest
    fun `should return register view if registration form contains invalid password  without performing registration`(invalidPassword: String) {
        mockMvc.post("/register"){
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("email","test@email.com")
            param("password",invalidPassword)
            param("repeatedPassword",invalidPassword)
            with(csrf())
        }.andExpect {
            status { isOk() }
            view { name("register") }
            model {
                hasErrors()
                attributeHasFieldErrorCode("registrationForm","password","Pattern")
                errorCount(1)
            }
        }

        verifyAll { userServiceMock wasNot called }
    }

    @MethodSource("invalidEmails")
    @ParameterizedTest
    fun `should return register view if registration form contains invalid email without performing registration`(invalidEmail: String) {
        mockMvc.post("/register"){
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("email",invalidEmail)
            param("password","ValidPass1")
            param("repeatedPassword","ValidPass1")
            with(csrf())
        }.andExpect {
            status { isOk() }
            view { name("register") }
            model {
                hasErrors()
                attributeHasFieldErrorCode("registrationForm","email","Email")
                errorCount(1)
            }
        }

        verifyAll { userServiceMock wasNot called }
    }

    @Test
    fun `should return register view with an error if registration throws DataIntegrityViolationException`() {
        every { userServiceMock.register(validRegistrationForm) } throws DataIntegrityViolationException("duplicate email")

        mockMvc.post("/register"){
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("email",validRegistrationForm.email)
            param("password",validRegistrationForm.password)
            param("repeatedPassword",validRegistrationForm.repeatedPassword)
            with(csrf())
        }.andExpect {
            status { isOk() }
            view { name("register") }
            model {
                hasErrors()
                errorCount(1)
                attributeHasFieldErrorCode("registrationForm","email","RegistrationForm.duplicate.email.message")
            }
        }

        verifyAll { userServiceMock.register(validRegistrationForm) }
    }

    @Test
    fun `should return register view with success message on successful registration`() {
        every { userServiceMock.register(validRegistrationForm) } just runs

        mockMvc.post("/register"){
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("email",validRegistrationForm.email)
            param("password",validRegistrationForm.password)
            param("repeatedPassword",validRegistrationForm.repeatedPassword)
            with(csrf())
        }.andExpect {
            status { isOk() }
            view { name("register") }
            model {
                hasNoErrors()
                attributeExists("success")
            }
        }

        verifyAll { userServiceMock.register(validRegistrationForm) }
    }
}