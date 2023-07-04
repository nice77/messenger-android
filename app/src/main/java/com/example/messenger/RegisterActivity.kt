package com.example.messenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var goToLoginButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginEditText = findViewById(R.id.loginNameEditText)
        registerButton = findViewById(R.id.registerButton)
        goToLoginButton = findViewById(R.id.btnGoToLogin)

        firebaseAuth = FirebaseAuth.getInstance()
        usersRef = FirebaseDatabase.getInstance().getReference("users")


        val validation = Validation()

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val login = loginEditText.text.toString()

            if (validation.isEmailValid(email)) {
                println("Почта верная")
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

            if (validation.isLoginName(login)) {
                println("Логин верный")
            } else {
                loginEditText.error = "Неверный логин"
                return@setOnClickListener
            }

            register(email, password, login)
        }

        goToLoginButton.setOnClickListener {
            goToLogin()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun register(email: String, password: String, loginName: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Создаем слушатель AuthStateListener
                    val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                        val firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser != null) {
                            val userId = firebaseUser.uid

                            // Создаем объект пользователя
                            val user = User(loginName, userId, email)

                            // Добавляем пользователя в базу данных
                            usersRef.child(userId).setValue(user)

                            // Регистрация успешна, переходим на следующую активити
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    // Добавляем слушатель к FirebaseAuth
                    firebaseAuth.addAuthStateListener(authStateListener)
                } else {
                    // Возникла ошибка при регистрации
                    Log.e(TAG, "createUserWithEmailAndPassword:failure", task.exception)
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
