package com.example.zequiz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zequiz.R
import com.example.zequiz.api.ApiService
import com.example.zequiz.models.LoginRequest
import com.example.zequiz.models.LoginResponse
import com.example.zequiz.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var daftarButton: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sessionManager = SessionManager(this)

        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        daftarButton = findViewById(R.id.buttonDaftar)

        loginButton.setOnClickListener {
            loginUser()
        }

        daftarButton.setOnClickListener {
            val intent = Intent(this, DaftarActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val loginRequest = LoginRequest(username, password)

        ApiService.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()!!
                    sessionManager.saveAuthToken(loginResponse.token)
                    sessionManager.saveUsername(loginResponse.username)
                    sessionManager.saveRole(loginResponse.role)

                    if (loginResponse.role == "SISWA") {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    } else if (loginResponse.role == "GURU") {
                        startActivity(Intent(this@LoginActivity, MainGuruActivity::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login gagal. Cek username/password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Login error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
