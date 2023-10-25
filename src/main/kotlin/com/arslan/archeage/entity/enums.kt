package com.arslan.archeage.entity

import java.util.Locale

enum class UserRole { ADMIN, USER }

enum class SupportedLanguages(val locale: Locale) {
    ENGLISH(Locale.ENGLISH), RUSSIAN(Locale("ru","RU"));
}

enum class Region{ EUROPE,CIS }