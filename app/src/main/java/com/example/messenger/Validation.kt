package com.example.messenger
import java.util.regex.Pattern

class Validation {
    fun isLoginValid(login: String): Boolean {
        val loginRegex = "^[a-zA-Z0-9_]{3,20}$"
        val pattern = Pattern.compile(loginRegex)
        val matcher = pattern.matcher(login)
        return matcher.matches()
    }

    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
        val pattern = Pattern.compile(passwordRegex)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}