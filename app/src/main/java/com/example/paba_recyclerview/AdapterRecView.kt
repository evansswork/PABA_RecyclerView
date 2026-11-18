package com.example.paba_recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paba_recyclerview.databinding.ItemRecyclerBinding
import coil.load
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import android.widget.Toast
class AdapterRecView(
    private val listWayang: ArrayList<DcWayang>
) : RecyclerView.Adapter<AdapterRecView.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: DcWayang)
        fun delData(position: Int)
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }
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

        val circularProgress = CircularProgressDrawable(holder.itemView.context).apply {
            strokeWidth = 5f
            centerRadius = 20f
            start()
        }

        holder.binding.gambarWayang.load(wayang.foto) {
            placeholder(circularProgress)
            error(android.R.drawable.ic_menu_report_image)
        }

        holder.binding.namaWayang.setOnClickListener {
            onItemClickCallback.onItemClicked(wayang)
        }

        holder.binding.gambarWayang.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                wayang.nama,
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.binding.btnHapus.setOnClickListener {
            val positionSaatDiklik = holder.bindingAdapterPosition

            if (positionSaatDiklik != RecyclerView.NO_POSITION) {
                onItemClickCallback.delData(positionSaatDiklik)
            }
        }
    }
}