package com.example.messenger.messanger

import org.threeten.bp.LocalDateTime

data class Messeng(
    val id: Long = 0,
    val from: String = "",
    val msg: String = "",
    val date_time: LocalDateTime = LocalDateTime.now()
)

