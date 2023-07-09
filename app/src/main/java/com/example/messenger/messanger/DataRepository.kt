package com.example.messenger.messanger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.messenger.ui.chats.ChatsFragment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.util.LinkedList
import java.util.concurrent.CountDownLatch

class DataRepository private constructor() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val besedaLiveData = MutableLiveData<List<Beseda>>()
    private val databaseRef: DatabaseReference = database.reference
    private var users: MutableList<User>? = null
    private var besedas = mutableListOf<Beseda>()
    private var chatsFragment: ChatsFragment? = null
    private var user = ""

    companion object {
        private var instance: DataRepository? = null

        fun getInstance(): DataRepository {
            if (instance == null) {
                instance = DataRepository()
            }
            return instance as DataRepository
        }
    }

    fun setChatsFragment(fragment: ChatsFragment) {
        chatsFragment = fragment
        listenForBesedaChanges(user)
    }

    fun addBeseda(ids: Beseda) {
        this.besedas.add(ids)
    }


    fun getBesedasLiveData() : LiveData<List<Beseda>> {
        return besedaLiveData
    }

    fun setUser(uid: String) {
        synchronized(this) {
            this.user = uid
            println("User: " + user)
            getBesedas()
        }
    }

    fun getUser() : String {
        synchronized(this) {
            return this.user
        }
    }

    fun getBesedas(): List<Beseda>? {
        synchronized(this) {
            if (this.besedas.isNotEmpty()) {
                return this.besedas
            }
            getBesedasForUser(user) { fetchedBesedas ->
                fetchedBesedas?.let {
                    this.besedas.addAll(it)
                }
            }
            return besedas
        }
    }


    fun setBesedas(b : List<Beseda>) {
        synchronized(this) {
            this.besedas = b as MutableList<Beseda>
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
                    dataSnapshot.children.forEach { snapshot ->
                        val login = snapshot.child("login").getValue(String::class.java)
                        val id = snapshot.child("id").getValue(String::class.java)
                        val email = snapshot.child("email").getValue(String::class.java)
                        val fcmtoken = snapshot.child("fcmtoken").getValue(String::class.java)

                        val besedasId = mutableListOf<Int>()
                        snapshot.child("beseda").children.mapNotNullTo(besedasId) { it.getValue(Int::class.java) }

                        val user = User(login ?: "", id ?: "", email ?: "", fcmtoken ?: "", besedasId)
                        userList.add(user)
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

    fun listenForBesedaChanges(userId: String) {
        val userRef = databaseRef.child("users").child(userId)
        userRef.child("beseda").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val besedaIds = dataSnapshot.children.mapNotNull { it.getValue(Int::class.java) }
                fetchBesedasByIds(besedaIds) { besedas ->
                    // Обновляем список бесед во фрагменте
                    besedas?.let {
                        chatsFragment?.updateBesedas(it)
                    }
                    // Обновляем список бесед в DataRepository
                    setBesedas(besedas ?: emptyList())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при загрузке бесед: ${databaseError.message}")
            }
        })
    }

    fun hasCommonBesedaWithUser(user: User): Int? {
        val currentUserBesedas = getBesedas() ?: return null
        val userBesedas = user.beseda
        for (currentUserBeseda in currentUserBesedas) {
            for (userBeseda in userBesedas) {
                if (currentUserBeseda.besedaId == userBeseda) {
                    return currentUserBeseda.besedaId
                }
            }
        }
        return null
    }




    fun getBesedasForUser(userId: String, callback: (List<Beseda>?) -> Unit) {
        val userRef = databaseRef.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val besedaIds = dataSnapshot.child("beseda").children.mapNotNull { it.getValue(Int::class.java) }
                fetchBesedasByIds(besedaIds, callback)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при загрузке бесед: ${databaseError.message}")
                callback(null)
            }
        })
    }

    fun getUsers(): MutableList<User>? {
        if (this.users == null) {
            fetchUsersFromDatabase {
                it?.let { it1 -> this.users?.addAll(it1) }
            }
        }
        return this.users
    }

    fun setUsers() {
        fetchUsersFromDatabase {
            it?.let { it1 -> this.users?.addAll(it1) }
        }
    }





    fun fetchBesedasByIds(besedaIds: List<Int>, callback: (List<Beseda>?) -> Unit) {
        val besedasRef = databaseRef.child("besedas")
        besedasRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userList = mutableListOf<Beseda>()

                dataSnapshot.children.forEach { snapshot ->
                    val id = snapshot.child("besedaId").getValue(Int::class.java)
                    val besedasId = mutableListOf<Messeng>()
                    snapshot.child("messengs").children.mapNotNullTo(besedasId) { it.getValue(Messeng::class.java) }

                    val user = id?.let { Beseda(besedasId, it) }
                    user?.let { userList.add(it) }
                }
                callback(userList)
//                val besedaList = dataSnapshot.children.mapNotNull { it.getValue(Beseda::class.java) }
//                    .filter { it.besedaId in besedaIds }
//                callback(besedaList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Ошибка при загрузке бесед: ${databaseError.message}")
                callback(null)
            }
        })
    }

}