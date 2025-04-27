package com.example.zequiz.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.zequiz.R
import com.example.zequiz.activities.SkorActivity
import com.example.zequiz.api.ApiService
import com.example.zequiz.models.SoalResponse
import com.example.zequiz.models.SubmitJawabanRequest
import com.example.zequiz.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailSoalFragment : Fragment() {

    private lateinit var textViewPertanyaan: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var opsiA: RadioButton
    private lateinit var opsiB: RadioButton
    private lateinit var opsiC: RadioButton
    private lateinit var opsiD: RadioButton
    private lateinit var buttonNext: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonSubmit: Button
    private lateinit var timerTextView: TextView

    private lateinit var sessionManager: SessionManager
    private var soalList = listOf<SoalResponse>()
    private var currentIndex = 0
    private var jawabanUser = mutableListOf<String>()
    private var kuisId: Long = 0
    private var timer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_soal, container, false)

        sessionManager = SessionManager(requireContext())

        textViewPertanyaan = view.findViewById(R.id.textViewPertanyaan)
        radioGroup = view.findViewById(R.id.radioGroupPilihan)
        opsiA = view.findViewById(R.id.radioA)
        opsiB = view.findViewById(R.id.radioB)
        opsiC = view.findViewById(R.id.radioC)
        opsiD = view.findViewById(R.id.radioD)
        buttonNext = view.findViewById(R.id.buttonNext)
        buttonBack = view.findViewById(R.id.buttonBack)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)
        timerTextView = view.findViewById(R.id.textViewTimer)

        buttonNext.setOnClickListener { nextSoal() }
        buttonBack.setOnClickListener { previousSoal() }
        buttonSubmit.setOnClickListener { submitJawaban() }

        kuisId = arguments?.getLong("kuisId") ?: 0

        loadSoal()

        return view
    }

    private fun loadSoal() {
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        ApiService.instance.getSoalByTopik(token, kuisId).enqueue(object : Callback<List<SoalResponse>> {
            override fun onResponse(call: Call<List<SoalResponse>>, response: Response<List<SoalResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    soalList = response.body()!!
                    jawabanUser = MutableList(soalList.size) { "" }
                    showSoal()
                    startTimer(60) // 60 menit default, bisa disesuaikan
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil soal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<SoalResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSoal() {
        val soal = soalList[currentIndex]
        textViewPertanyaan.text = soal.pertanyaan
        opsiA.text = soal.opsiA
        opsiB.text = soal.opsiB
        opsiC.text = soal.opsiC
        opsiD.text = soal.opsiD
        radioGroup.clearCheck()

        // restore jawaban sebelumnya kalau ada
        when (jawabanUser[currentIndex]) {
            soal.opsiA -> opsiA.isChecked = true
            soal.opsiB -> opsiB.isChecked = true
            soal.opsiC -> opsiC.isChecked = true
            soal.opsiD -> opsiD.isChecked = true
        }
    }

    private fun nextSoal() {
        saveJawaban()
        if (currentIndex < soalList.size - 1) {
            currentIndex++
            showSoal()
        }
    }

    private fun previousSoal() {
        saveJawaban()
        if (currentIndex > 0) {
            currentIndex--
            showSoal()
        }
    }

    private fun saveJawaban() {
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId
        val selectedOption = view?.findViewById<RadioButton>(checkedRadioButtonId)
        jawabanUser[currentIndex] = selectedOption?.text.toString()
    }

    private fun submitJawaban() {
        saveJawaban()
        val token = "Bearer ${sessionManager.fetchAuthToken()}"
        val submitRequest = SubmitJawabanRequest(
            kuisId = kuisId,
            jawabanList = jawabanUser
        )
        ApiService.instance.submitJawaban(token, submitRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Jawaban dikirim!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), SkorActivity::class.java))
                    activity?.finish()
                } else {
                    Toast.makeText(requireContext(), "Gagal kirim jawaban", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startTimer(minutes: Int) {
        timer = object : CountDownTimer(minutes * 60 * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutesLeft = (millisUntilFinished / 1000) / 60
                val secondsLeft = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutesLeft, secondsLeft)
            }

            override fun onFinish() {
                Toast.makeText(requireContext(), "Waktu habis!", Toast.LENGTH_SHORT).show()
                submitJawaban()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
