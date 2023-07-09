package com.example.messenger.messanger

import org.threeten.bp.LocalDateTime

data class Messeng(
    val id: Long = 0,
    val from: String = "",
    val msg: String = "",
    val date_time: String = LocalDateTime.now().toString()
) {
    fun kek() {
        println(LocalDateTime.parse(this.date_time).compareTo(LocalDateTime.now()))
    }
}
