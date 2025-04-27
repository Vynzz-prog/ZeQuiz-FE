package com.example.zequiz.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zequiz.R
import com.example.zequiz.activities.BuatSoalActivity
import com.example.zequiz.adapters.SoalAdapter
import com.example.zequiz.api.ApiService
import com.example.zequiz.models.SoalResponse
import com.example.zequiz.models.TopikResponse
import com.example.zequiz.utils.SessionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BankSoalFragment : Fragment() {

    private lateinit var spinnerTopik: Spinner
    private lateinit var soalRecyclerView: RecyclerView
    private lateinit var sessionManager: SessionManager
    private lateinit var soalAdapter: SoalAdapter
    private lateinit var fabTambahSoal: FloatingActionButton
    private val soalList = mutableListOf<SoalResponse>()
    private var selectedTopikId: Long? = null
    private val topikList = mutableListOf<TopikResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bank_soal, container, false)

        sessionManager = SessionManager(requireContext())

        spinnerTopik = view.findViewById(R.id.spinnerTopik)
        soalRecyclerView = view.findViewById(R.id.recyclerViewSoal)
        fabTambahSoal = view.findViewById(R.id.fabTambahSoal)

        soalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        soalAdapter = SoalAdapter(soalList)
        soalRecyclerView.adapter = soalAdapter

        fabTambahSoal.setOnClickListener {
            startActivity(Intent(requireContext(), BuatSoalActivity::class.java))
        }

        loadTopik()

        spinnerTopik.setOnItemSelectedListener { position ->
            selectedTopikId = topikList[position].id
            loadSoalByTopik(selectedTopikId!!)
        }

        return view
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

    private fun loadSoalByTopik(topikId: Long) {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        ApiService.instance.getSoalByTopik(token, topikId).enqueue(object : Callback<List<SoalResponse>> {
            override fun onResponse(call: Call<List<SoalResponse>>, response: Response<List<SoalResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    soalList.clear()
                    soalList.addAll(response.body()!!)
                    soalAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil soal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SoalResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
