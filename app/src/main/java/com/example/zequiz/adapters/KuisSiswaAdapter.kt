package com.example.zequiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zequiz.R
import com.example.zequiz.models.KuisResponse

class KuisSiswaAdapter(
    private val kuisList: List<KuisResponse>,
    private val onItemClick: (KuisResponse) -> Unit
) : RecyclerView.Adapter<KuisSiswaAdapter.KuisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kuis_siswa, parent, false)
        return KuisViewHolder(view)
    }

    override fun onBindViewHolder(holder: KuisViewHolder, position: Int) {
        val kuis = kuisList[position]
        holder.bind(kuis)
        holder.itemView.setOnClickListener {
            onItemClick(kuis)
        }
    }

    override fun getItemCount(): Int = kuisList.size

    class KuisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaKuisText: TextView = itemView.findViewById(R.id.textViewNamaKuisSiswa)
        private val topikKuisText: TextView = itemView.findViewById(R.id.textViewTopikKuisSiswa)
        private val statusKuisText: TextView = itemView.findViewById(R.id.textViewStatusKuisSiswa)

        fun bind(kuis: KuisResponse) {
            namaKuisText.text = kuis.namaKuis
            topikKuisText.text = kuis.topikNama
            statusKuisText.text = if (kuis.sudahDikerjakan) "Sudah dikerjakan" else "Belum dikerjakan"
        }
    }
}
