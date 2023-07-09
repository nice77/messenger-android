package com.example.messenger

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val fbDB = FirebaseDatabase.getInstance().reference
    private val SWITCH_PREFS: String = "SWITCH"
    private lateinit var sharedPreferences: SharedPreferences
    private val PREF_FIRST_RUN = "first_run"
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chats, R.id.navigation_dialog, R.id.navigation_settings
            )
        )


//        Включить как только разберемся с темной темой
                unit()


        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            DataRepository.getInstance().setUser(currentUser.uid)
            DataRepository.getInstance().getBesedasForUser(currentUser.uid) { besedas ->
                if (besedas != null) {
                    loadingFragment(besedas)
                }
            }

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val isFirstRun = sharedPreferences.getBoolean(PREF_FIRST_RUN, true)
            DataRepository.getInstance().fetchUsersFromDatabase {}

            if (isFirstRun) {
                Snackbar.make(
                    binding.root,
                    "Приветствую ${currentUser.email}",
                    Snackbar.LENGTH_LONG
                ).show()
                sharedPreferences.edit().putBoolean(PREF_FIRST_RUN, false).apply()
            }
        }
    }

    private fun unit() {
        val pref = getSharedPreferences("Default", MODE_PRIVATE)
        if (pref.getBoolean(SWITCH_PREFS, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    fun switchTheme(context: Activity, isDarkTheme: Boolean) {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        context.recreate()
    }

    fun loadingFragment(besedas: List<Beseda>) {
        DataRepository.getInstance().setBesedas(besedas)

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(binding.navView, navController, false)
    }
}

