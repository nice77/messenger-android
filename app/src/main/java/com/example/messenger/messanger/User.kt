package com.example.messenger.messanger

data class User(val login: String = "", val id: String = "", val email: String = "", val fcmtoken: String = "", val beseda: List<Int> = emptyList()) {
    // Добавьте пустой конструктор без аргументов
    constructor() : this("", "", "", "")
}
