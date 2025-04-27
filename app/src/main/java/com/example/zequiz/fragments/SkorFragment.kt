package com.example.zequiz.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.zequiz.R
import com.example.zequiz.utils.SessionManager

class SkorFragment : Fragment() {

    private lateinit var textViewNama: TextView
    private lateinit var textViewKelas: TextView
    private lateinit var textViewSkor: TextView
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_skor, container, false)

        sessionManager = SessionManager(requireContext())

        textViewNama = view.findViewById(R.id.textViewNamaSiswa)
        textViewKelas = view.findViewById(R.id.textViewKelasSiswa)
        textViewSkor = view.findViewById(R.id.textViewNilaiSiswa)

        loadDataSkor()

        return view
    }

    private fun loadDataSkor() {
        val nama = sessionManager.fetchUsername()
        val kelas = "Kelas ${sessionManager.fetchKelas() ?: "?"}"
        val skor = arguments?.getInt("skor") ?: 0

        textViewNama.text = nama
        textViewKelas.text = kelas
        textViewSkor.text = skor.toString()
    }
}
