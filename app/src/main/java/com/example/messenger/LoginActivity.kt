package com.example.messenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var goToRegisterButton: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        goToRegisterButton = findViewById(R.id.btnGoToRegister)

        val validation = Validation()

        firebaseAuth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {


            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validation.isEmailValid(email)) {
                println("Логин верный")
            } else {
                emailEditText.error = "Неверная почта"
                return@setOnClickListener
            }

            if (validation.isPasswordValid(password)) {
                println("Пароль верный")
            } else {
                passwordEditText.error = "Пароль неверный"
                return@setOnClickListener
            }

            login(email, password)
        }

        goToRegisterButton.setOnClickListener {
            goToRegistration()
        }
    }

    private fun goToRegistration() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Авторизация успешна, переходим на следующую активити
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Возникла ошибка при авторизации
                    Log.e(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Ошибка при авторизации.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
