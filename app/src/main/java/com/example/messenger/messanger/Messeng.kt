package com.example.messenger.messanger

import java.time.LocalDateTime

data class Messeng(val id: Long, val from: Long, val toUser: Long, val msg: String, val date_time: LocalDateTime)