package com.example.messenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.messenger.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
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
            Snackbar.make(binding.root, "Приветствую ${currentUser.email}", Snackbar.LENGTH_LONG)
                .show()

//            val dataRepository = DataRepository.getInstance()
//            dataRepository.fetchUsersFromDatabase {data ->
//                println(data!=null)
//            }

            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            binding.navView.setupWithNavController(navController)
            NavigationUI.setupWithNavController(binding.navView, navController, false)
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

}
