package com.example.paba_recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paba_recyclerview.databinding.ItemRecyclerBinding
import coil.load

class AdapterRecView(
    private val listWayang: ArrayList<DcWayang>
) : RecyclerView.Adapter<AdapterRecView.ListViewHolder>() {

    inner class ListViewHolder(
        val binding: ItemRecyclerBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRecyclerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listWayang.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val wayang = listWayang[position]

        holder.binding.namaWayang.text = wayang.nama
        holder.binding.karakterWayang.text = wayang.karakter
        holder.binding.deskripsiWayang.text = wayang.deskripsi

        holder.binding.gambarWayang.load(wayang.foto) {
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }
    }
}