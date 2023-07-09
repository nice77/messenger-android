package com.example.messenger.messanger

data class Beseda(var messengs: List<Messeng> = emptyList(), var besedaId: Int) {
    constructor() : this(emptyList(), 0)
}