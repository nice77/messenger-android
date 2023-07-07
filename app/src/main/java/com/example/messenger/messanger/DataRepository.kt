package com.example.messenger.messanger

import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.util.concurrent.CountDownLatch

class DataRepository private constructor() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseRef: DatabaseReference = database.reference
    private var users: List<User>? = null
    private var besedas: List<Beseda>? = null

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
//                    for (snapshot in dataSnapshot.children) {
//
//                        val user = snapshot.getValue(User::class.java)
//                        user?.let { userList.add(it) }
//                    }
                    for (snapshot in dataSnapshot.children) {
                        val login = snapshot.child("login").getValue(String::class.java)
                        val id = snapshot.child("id").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val fcmtoken = snapshot.child("fcmtoken").getValue(String::class.java)

                        var besedasId = mutableListOf<Int>()

                        for (besedasChilfren in snapshot.child("beseda").children) {
                            val besedasI = besedasChilfren.getValue(Int::class.java)
                            besedasI?.let {besedasId.add(it)}
                        }
                        login?.let { id?.let { it1 -> email?.let { it2 ->
                            fcmtoken?.let { it3 ->
                                User(it, it1,
                                    it2, it3, besedasId)
                            }
                        } } }
                            ?.let { userList.add(it) }

//                        val user = User(login = login, id = id, email = email, fcmtoken = fcmtoken)
//                        val user = snapshot.getValue(User::class.java)

//                        userList.add(user)
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

    fun fetchBesedasFromDatabase(userId: String, callback: (List<Beseda>?) -> Unit) {
        val userRef = databaseRef.child("users").child(userId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val besedaIds = dataSnapshot.child("beseda").children.map { it.getValue(Int::class.java) }
                fetchBesedasByIds(besedaIds, callback)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при загрузке бесед: ${databaseError.message}")
                callback(null)
            }
        })
    }

    private fun fetchBesedasByIds(besedaIds: List<Int?>, callback: (List<Beseda>?) -> Unit) {
        if (besedaIds == null || besedaIds.isEmpty()) {
            callback(emptyList())
            return
        }

        val besedasRef = databaseRef.child("besedas")
        val besedaList = mutableListOf<Beseda>()
        val countDownLatch = CountDownLatch(besedaIds.size)

        for (besedaId in besedaIds) {
            val besedaRef = besedasRef.child(besedaId.toString())
            besedaRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val beseda = dataSnapshot.getValue(Beseda::class.java)
                    beseda?.let { besedaList.add(it) }
                    countDownLatch.countDown()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Ошибка при загрузке беседы: ${databaseError.message}")
                    countDownLatch.countDown()
                }
            })
        }

        countDownLatch.await()
        besedas = besedaList
        callback(besedas)
    }
}
