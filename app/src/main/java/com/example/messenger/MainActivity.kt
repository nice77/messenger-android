package com.example.messenger

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.ContactsContract.Data
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.messanger.Beseda
import com.example.messenger.messanger.DataRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private lateinit var firebaseAuth: FirebaseAuth
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val fbDB = FirebaseDatabase.getInstance().reference
    private val currUser = firebaseAuth.currentUser
    private val SWITCH_PREFS: String = "SWITCH"
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_FIRST_RUN = "first_run"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        unit()
        if (currUser != null) {
            DataRepository.getInstance().setUser(currUser.uid)
            DataRepository.getInstance().getBesedasForUser(DataRepository.getInstance().getUser()) {
                it?.let { it1 -> loadingFragment(it1) }
            }
//            for (snapshot in fbDB.child("users").child(currUser.uid).child("beseda")) {
//
//            }
            println("Besedas called in main activity: " + DataRepository.getInstance().getBesedas())
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        //где-то тут вызываются пользовательские чаты

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Проверяем, авторизован ли пользователь
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Пользователь не авторизован, переходим на экран авторизации
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)


            finish()
        } else {
            // Пользователь авторизован, выполняем нужные действия
//            downloadDataFromDatabase(this)
            println("Setting user " + currentUser.uid)
            DataRepository.getInstance().setUser(currentUser.uid)

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val isFirstRun = sharedPreferences.getBoolean(PREF_FIRST_RUN, true)
            DataRepository.getInstance().fetchUsersFromDatabase {}

            if (isFirstRun) {
                Snackbar.make(
                    binding.root,
                    "Приветствую ${currentUser.email}",
                    Snackbar.LENGTH_LONG
                )
                    .show()
                sharedPreferences.edit().putBoolean(PREF_FIRST_RUN, false).apply()
            }


//            val dataRepository = DataRepository.getInstance()
//            dataRepository.fetchUsersFromDatabase {data ->
//                println(data!=null)
//            }
        }
    }

    //    fun downloadDataFromDatabase(context: Context) {
//        val database = FirebaseDatabase.getInstance()
//        val databaseRef = database.reference
//
//        // Определяем имя файла для сохранения данных
//        val fileName = "data.json"
//
//        // Получаем путь к внутренней директории приложения
//        val internalDir = context.filesDir
//
//        // Создаем объект файла
//        val file = File(internalDir, fileName)
//
//        // Запускаем корутину для выполнения операции в фоновом потоке
//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                // Получаем данные из Realtime Database
//                val dataSnapshot = databaseRef.get().await()
//                val json = dataSnapshot.getValue<String>()
//
//                // Сохраняем данные в JSON файл
//                FileWriter(file).use { writer ->
//                    writer.write(json ?: "")
//                }
//
//                println("Данные успешно скачаны и сохранены в файл $fileName")
//            } catch (e: Exception) {
//                println("Ошибка при скачивании данных: ${e.message}")
//            }
//        }
//    }
    private fun unit() {
        val pref = getSharedPreferences("Default", MODE_PRIVATE)
        if (pref.getBoolean(SWITCH_PREFS, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

    }


    // Функция для смены темы
    fun switchTheme(context: Activity, isDarkTheme: Boolean) {
        // Устанавливаем режим темы
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        // Пересоздаем активность, чтобы применить изменения
        context.recreate()
    }

    fun loadingFragment(besedasId : List<Beseda>) {
        DataRepository.getInstance().setBesedas(besedasId)
        val b = mutableListOf<Beseda>()
        println(DataRepository.getInstance().getBesedas())
        println("Arg: " + besedasId)
        println("B: " + b)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chats, R.id.navigation_dialog, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.navView, navController, false)
    }
}
