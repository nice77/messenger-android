package com.example.messenger

import com.google.firebase.database.*

class DataRepository private constructor() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseRef: DatabaseReference = database.reference
    private var users: List<User>? = null
    private var messages: List<Messeng>? = null

    companion object {
        private var instance: DataRepository? = null

        fun getInstance(): DataRepository {
            if (instance == null) {
                instance = DataRepository()
            }
            return instance as DataRepository
        }
    }

    fun fetchUsersFromDatabase(callback: (List<User>?) -> Unit) {
        if (users != null) {
            // Если пользователи уже загружены, возвращаем их сразу
            callback(users)
        } else {
            // Загружаем пользователей из базы данных
            val usersRef = databaseRef.child("users")
            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userList = mutableListOf<User>()
                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let { userList.add(it) }
                    }
                    users = userList
                    callback(users)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Ошибка при загрузке пользователей: ${databaseError.message}")
                    callback(null)
                }
            })
        }
    }

    fun fetchMessagesFromDatabase(callback: (List<Messeng>?) -> Unit) {
        if (messages != null) {
            // Если сообщения уже загружены, возвращаем их сразу
            callback(messages)
        } else {
            // Загружаем сообщения из базы данных
            val messagesRef = databaseRef.child("messages")
            messagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val messageList = mutableListOf<Messeng>()
                    for (snapshot in dataSnapshot.children) {
                        val message = snapshot.getValue(Messeng::class.java)
                        message?.let { messageList.add(it) }
                    }
                    messages = messageList
                    callback(messages)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Ошибка при загрузке сообщений: ${databaseError.message}")
                    callback(null)
                }
            })
        }
    }
}
