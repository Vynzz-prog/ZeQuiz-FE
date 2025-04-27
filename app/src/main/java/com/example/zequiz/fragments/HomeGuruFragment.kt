package com.example.zequiz.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zequiz.R
import com.example.zequiz.activities.DaftarSkorActivity
import com.example.zequiz.adapters.KuisGuruAdapter
import com.example.zequiz.api.ApiService
import com.example.zequiz.models.KuisResponse
import com.example.zequiz.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeGuruFragment : Fragment() {

    private lateinit var kuisRecyclerView: RecyclerView
    private lateinit var sessionManager: SessionManager
    private lateinit var kuisAdapter: KuisGuruAdapter
    private val kuisList = mutableListOf<KuisResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_guru, container, false)

        sessionManager = SessionManager(requireContext())

        kuisRecyclerView = view.findViewById(R.id.recyclerViewKuisGuru)
        kuisRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        kuisAdapter = KuisGuruAdapter(kuisList) { kuis ->
            val intent = Intent(requireContext(), DaftarSkorActivity::class.java)
            intent.putExtra("kuisId", kuis.id)
            startActivity(intent)
        }
        kuisRecyclerView.adapter = kuisAdapter

        loadKuisGuru()

        return view
    }

    private fun loadKuisGuru() {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        ApiService.instance.getKuisGuru(token).enqueue(object : Callback<List<KuisResponse>> {
            override fun onResponse(call: Call<List<KuisResponse>>, response: Response<List<KuisResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    kuisList.clear()
                    kuisList.addAll(response.body()!!)
                    kuisAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil data kuis", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<KuisResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
