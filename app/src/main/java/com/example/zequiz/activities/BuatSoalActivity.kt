package com.example.zequiz.activities

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.zequiz.R
import com.example.zequiz.api.ApiService
import com.example.zequiz.models.SoalRequest
import com.example.zequiz.models.TopikResponse
import com.example.zequiz.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuatSoalActivity : AppCompatActivity() {

    private lateinit var editTextPertanyaan: EditText
    private lateinit var editTextOpsiA: EditText
    private lateinit var editTextOpsiB: EditText
    private lateinit var editTextOpsiC: EditText
    private lateinit var editTextOpsiD: EditText
    private lateinit var editTextJawabanBenar: EditText
    private lateinit var spinnerTopik: Spinner
    private lateinit var buttonSimpanSoal: Button
    private lateinit var buttonBack: ImageButton
    private lateinit var sessionManager: SessionManager
    private var topikList = mutableListOf<TopikResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buat_soal)

        sessionManager = SessionManager(this)

        editTextPertanyaan = findViewById(R.id.editTextPertanyaan)
        editTextOpsiA = findViewById(R.id.editTextOpsiA)
        editTextOpsiB = findViewById(R.id.editTextOpsiB)
        editTextOpsiC = findViewById(R.id.editTextOpsiC)
        editTextOpsiD = findViewById(R.id.editTextOpsiD)
        editTextJawabanBenar = findViewById(R.id.editTextJawabanBenar)
        spinnerTopik = findViewById(R.id.spinnerTopikBuatSoal)
        buttonSimpanSoal = findViewById(R.id.buttonSimpanSoal)
        buttonBack = findViewById(R.id.buttonBackBuatSoal)

        buttonBack.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi")
            builder.setMessage("Kembali akan membatalkan pembuatan soal. Lanjutkan?")
            builder.setPositiveButton("Ya") { _, _ ->
                finish()
            }
            builder.setNegativeButton("Tidak", null)
            builder.show()
        }

        buttonSimpanSoal.setOnClickListener {
            tambahSoal()
        }

        loadTopik()
    }

    private fun loadTopik() {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        ApiService.instance.getAllTopik(token).enqueue(object : Callback<List<TopikResponse>> {
            override fun onResponse(call: Call<List<TopikResponse>>, response: Response<List<TopikResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    topikList.clear()
                    topikList.addAll(response.body()!!)
                    val topikNames = topikList.map { it.nama }
                    spinnerTopik.adapter = ArrayAdapter(this@BuatSoalActivity, android.R.layout.simple_spinner_dropdown_item, topikNames)
                } else {
                    Toast.makeText(this@BuatSoalActivity, "Gagal mengambil data topik", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TopikResponse>>, t: Throwable) {
                Toast.makeText(this@BuatSoalActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun tambahSoal() {
        val pertanyaan = editTextPertanyaan.text.toString().trim()
        val opsiA = editTextOpsiA.text.toString().trim()
        val opsiB = editTextOpsiB.text.toString().trim()
        val opsiC = editTextOpsiC.text.toString().trim()
        val opsiD = editTextOpsiD.text.toString().trim()
        val jawabanBenar = editTextJawabanBenar.text.toString().trim()

        if (pertanyaan.isEmpty() || opsiA.isEmpty() || opsiB.isEmpty() || opsiC.isEmpty() || opsiD.isEmpty() || jawabanBenar.isEmpty()) {
            Toast.makeText(this, "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedTopik = topikList[spinnerTopik.selectedItemPosition]

        val soalRequest = SoalRequest(
            pertanyaan,
            opsiA,
            opsiB,
            opsiC,
            opsiD,
            jawabanBenar,
            null, // gambar (opsional, bisa null)
            selectedTopik.id
        )

        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        ApiService.instance.tambahSoal(token, soalRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@BuatSoalActivity, "Soal berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@BuatSoalActivity, "Gagal menambah soal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@BuatSoalActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
