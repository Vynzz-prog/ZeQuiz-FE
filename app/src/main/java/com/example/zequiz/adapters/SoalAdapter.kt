package com.example.zequiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zequiz.R
import com.example.zequiz.models.SoalResponse

class SoalAdapter(
    private val soalList: List<SoalResponse>
) : RecyclerView.Adapter<SoalAdapter.SoalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_soal, parent, false)
        return SoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoalViewHolder, position: Int) {
        val soal = soalList[position]
        holder.bind(soal)
    }

    override fun getItemCount(): Int = soalList.size

    class SoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewPertanyaan: TextView = itemView.findViewById(R.id.textViewPertanyaanSoal)

        fun bind(soal: SoalResponse) {
            textViewPertanyaan.text = soal.pertanyaan
        }
    }
}
