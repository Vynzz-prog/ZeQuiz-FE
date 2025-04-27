package com.example.zequiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zequiz.R
import com.example.zequiz.models.SkorSiswaResponse

class SkorSiswaAdapter(
    private val skorList: List<SkorSiswaResponse>
) : RecyclerView.Adapter<SkorSiswaAdapter.SkorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skor_siswa, parent, false)
        return SkorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkorViewHolder, position: Int) {
        val skor = skorList[position]
        holder.bind(skor)
    }

    override fun getItemCount(): Int = skorList.size

    class SkorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNamaSiswa: TextView = itemView.findViewById(R.id.textViewNamaSiswaSkor)
        private val textViewSkorSiswa: TextView = itemView.findViewById(R.id.textViewNilaiSiswaSkor)

        fun bind(skor: SkorSiswaResponse) {
            textViewNamaSiswa.text = skor.namaSiswa
            textViewSkorSiswa.text = "${skor.nilai} poin"
        }
    }
}
