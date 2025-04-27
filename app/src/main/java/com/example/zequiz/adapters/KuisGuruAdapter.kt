package com.example.zequiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zequiz.R
import com.example.zequiz.models.KuisResponse

class KuisGuruAdapter(
    private val kuisList: List<KuisResponse>,
    private val onItemClick: (KuisResponse) -> Unit
) : RecyclerView.Adapter<KuisGuruAdapter.KuisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KuisViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kuis_guru, parent, false)
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
        private val namaKuisText: TextView = itemView.findViewById(R.id.textViewNamaKuisGuru)
        private val topikKuisText: TextView = itemView.findViewById(R.id.textViewTopikKuisGuru)

        fun bind(kuis: KuisResponse) {
            namaKuisText.text = kuis.namaKuis
            topikKuisText.text = kuis.topikNama
        }
    }
}
