package com.arslan.archeage.entity

import java.util.Locale

enum class UserRole { ADMIN, USER }

enum class SupportedLanguages(val locale: Locale,val displayName: String) {
    ENGLISH(Locale.ENGLISH,"English"), RUSSIAN(Locale("ru","RU"),"Русский");
}
