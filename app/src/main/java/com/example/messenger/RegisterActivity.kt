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

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var goToLoginButton: Button

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        goToLoginButton = findViewById(R.id.btnGoToLogin)

        firebaseAuth = FirebaseAuth.getInstance()
        val validation = Validation()

        registerButton.setOnClickListener {
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
                emailEditText.error = "Пароль неверный"
                return@setOnClickListener
            }

            register(email, password)
        }

        goToLoginButton.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun register(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Регистрация успешна, переходим на следующую активити
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Возникла ошибка при регистрации
                    Log.e(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this, "Ошибка при регистрации.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}
