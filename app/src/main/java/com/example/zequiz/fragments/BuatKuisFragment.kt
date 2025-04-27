package com.example.zequiz.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.zequiz.R
import com.example.zequiz.api.ApiService
import com.example.zequiz.models.BuatKuisRequest
import com.example.zequiz.models.TopikResponse
import com.example.zequiz.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuatKuisFragment : Fragment() {

    private lateinit var editTextNamaKuis: EditText
    private lateinit var spinnerJumlahSoal: Spinner
    private lateinit var spinnerTimer: Spinner
    private lateinit var spinnerTopik: Spinner
    private lateinit var buttonUnggahKuis: Button
    private lateinit var sessionManager: SessionManager
    private val topikList = mutableListOf<TopikResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buat_kuis, container, false)

        sessionManager = SessionManager(requireContext())

        editTextNamaKuis = view.findViewById(R.id.editTextNamaKuis)
        spinnerJumlahSoal = view.findViewById(R.id.spinnerJumlahSoal)
        spinnerTimer = view.findViewById(R.id.spinnerTimer)
        spinnerTopik = view.findViewById(R.id.spinnerTopikPilih)
        buttonUnggahKuis = view.findViewById(R.id.buttonUnggahKuis)

        buttonUnggahKuis.setOnClickListener {
            konfirmasiUpload()
        }

        setupSpinners()
        loadTopik()

        return view
    }

    private fun setupSpinners() {
        val jumlahSoalOptions = listOf(5, 10, 15, 20)
        val timerOptions = listOf("30 menit", "60 menit")

        spinnerJumlahSoal.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, jumlahSoalOptions)
        spinnerTimer.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, timerOptions)
    }

    private fun loadTopik() {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        ApiService.instance.getAllTopik(token).enqueue(object : Callback<List<TopikResponse>> {
            override fun onResponse(call: Call<List<TopikResponse>>, response: Response<List<TopikResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    topikList.clear()
                    topikList.addAll(response.body()!!)
                    val topikNames = topikList.map { it.nama }
                    spinnerTopik.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, topikNames)
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil data topik", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<TopikResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun konfirmasiUpload() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Konfirmasi")
        builder.setMessage("Apakah Anda yakin ingin mengunggah kuis ini?")
        builder.setPositiveButton("Ya") { _, _ ->
            unggahKuis()
        }
        builder.setNegativeButton("Tidak", null)
        builder.show()
    }

    private fun unggahKuis() {
        val namaKuis = editTextNamaKuis.text.toString().trim()
        val jumlahSoal = spinnerJumlahSoal.selectedItem.toString().toInt()
        val waktuMenit = if (spinnerTimer.selectedItem.toString() == "30 menit") 30 else 60
        val selectedTopik = topikList[spinnerTopik.selectedItemPosition]

        if (namaKuis.isEmpty()) {
            Toast.makeText(requireContext(), "Nama kuis tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val kuisRequest = BuatKuisRequest(
            namaKuis,
            jumlahSoal,
            waktuMenit,
            selectedTopik.id
        )

        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        ApiService.instance.buatKuis(token, kuisRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Kuis berhasil dibuat", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                } else {
                    Toast.makeText(requireContext(), "Gagal membuat kuis", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
