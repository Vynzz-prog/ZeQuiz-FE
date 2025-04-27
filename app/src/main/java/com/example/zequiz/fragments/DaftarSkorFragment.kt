package com.example.zequiz.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zequiz.R
import com.example.zequiz.adapters.SkorSiswaAdapter
import com.example.zequiz.api.ApiService
import com.example.zequiz.models.SkorSiswaResponse
import com.example.zequiz.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarSkorFragment : Fragment() {

    private lateinit var skorRecyclerView: RecyclerView
    private lateinit var skorAdapter: SkorSiswaAdapter
    private lateinit var sessionManager: SessionManager
    private val skorList = mutableListOf<SkorSiswaResponse>()
    private var kuisId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daftar_skor, container, false)

        sessionManager = SessionManager(requireContext())

        skorRecyclerView = view.findViewById(R.id.recyclerViewDaftarSkor)
        skorRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        skorAdapter = SkorSiswaAdapter(skorList)
        skorRecyclerView.adapter = skorAdapter

        kuisId = arguments?.getLong("kuisId") ?: 0

        loadDaftarSkor()

        return view
    }

    private fun loadDaftarSkor() {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        ApiService.instance.getSkorSiswa(token, kuisId).enqueue(object : Callback<List<SkorSiswaResponse>> {
            override fun onResponse(call: Call<List<SkorSiswaResponse>>, response: Response<List<SkorSiswaResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    skorList.clear()
                    skorList.addAll(response.body()!!)
                    skorAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil daftar skor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SkorSiswaResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
