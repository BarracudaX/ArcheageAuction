package com.arslan.archeage.service

import com.arslan.archeage.controllers.RegistrationForm

interface UserService {

    fun register(registrationForm: RegistrationForm)

}