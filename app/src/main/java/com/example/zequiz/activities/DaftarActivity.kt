package com.example.zequiz.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zequiz.R
import com.example.zequiz.api.ApiService
import com.example.zequiz.models.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var kelasEditText: EditText
    private lateinit var daftarButton: Button
    private lateinit var kembaliButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        usernameEditText = findViewById(R.id.editTextUsernameDaftar)
        passwordEditText = findViewById(R.id.editTextPasswordDaftar)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        kelasEditText = findViewById(R.id.editTextKelas)
        daftarButton = findViewById(R.id.buttonDaftarSubmit)
        kembaliButton = findViewById(R.id.buttonBackToLogin)

        daftarButton.setOnClickListener {
            registerUser()
        }

        kembaliButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser() {
        val username = usernameEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()
        val kelas = kelasEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || kelas.isEmpty()) {
            Toast.makeText(this, "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password dan konfirmasi tidak sama", Toast.LENGTH_SHORT).show()
            return
        }

        if (kelas != "4" && kelas != "5" && kelas != "6") {
            Toast.makeText(this, "Kelas hanya boleh 4, 5, atau 6", Toast.LENGTH_SHORT).show()
            return
        }

        val registerRequest = RegisterRequest(username, password, kelas.toInt())

        ApiService.instance.register(registerRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DaftarActivity, "Pendaftaran berhasil. Silakan login.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@DaftarActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@DaftarActivity, "Username sudah digunakan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@DaftarActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
