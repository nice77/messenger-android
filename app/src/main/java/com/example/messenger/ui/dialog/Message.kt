package com.example.messenger.ui.dialog

data class Message(val fromId: Int, val toId: Int, val msg: String, val timestamp: Int, val id: Int = 0)
