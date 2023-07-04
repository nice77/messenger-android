package com.example.messenger

data class User(val login: String = "", val id: String = "", val email: String = "") {
    // Добавьте пустой конструктор без аргументов
    constructor() : this("", "", "")
}
