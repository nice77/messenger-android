package com.example.messenger.messanger


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateBeseda {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseRef: DatabaseReference = database.reference


    fun createBeseda(userIds: List<String>?) {
        if (userIds != null) {
            val besedaId = generateBesedaId()
            val messages = emptyList<Messeng>()

            // Создаем объект Beseda
            val beseda = Beseda(messages, besedaId)

            // Загружаем беседу в базу данных
            val besedaRef = databaseRef.child("besedas").child(besedaId.toString())
            besedaRef.setValue(beseda)

            // Обновляем поле beseda у каждого пользователя
            for (userId in userIds) {
                val userRef = databaseRef.child("users").child(userId).child("beseda").push()
                userRef.setValue(besedaId)
            }
        }
    }

    

    private fun generateBesedaId(): Int {
        // Генерируем уникальный айди для беседы
        return (System.currentTimeMillis() / 1000).toInt()
    }

}
