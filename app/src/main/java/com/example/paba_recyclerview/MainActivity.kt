package com.example.paba_recyclerview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.paba_recyclerview.databinding.ActivityMainBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var fotoWayang: Array<String>
    private lateinit var namaWayang: Array<String>
    private lateinit var karakterWayang: Array<String>
    private lateinit var deskripsiWayang: Array<String>

    private val _arWayang = ArrayList<DcWayang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        siapkanData()
        tambahData()
        tampilkanData()

        binding.btnToggleLayout.setOnCheckedChangeListener { _, _ ->
            tampilkanData()
        }
    }

    private fun siapkanData() {
        fotoWayang = resources.getStringArray(R.array.gambarWayang)
        namaWayang = resources.getStringArray(R.array.namaWayang)
        karakterWayang = resources.getStringArray(R.array.karakterUtamaWayang)
        deskripsiWayang = resources.getStringArray(R.array.deskripsiWayang)
    }

    private fun tambahData() {
        for (position in namaWayang.indices) {
            val wayang = DcWayang(
                foto = fotoWayang[position],
                nama = namaWayang[position],
                karakter = karakterWayang[position],
                deskripsi = deskripsiWayang[position]
            )
            _arWayang.add(wayang)
        }
    }

    private fun tampilkanData() {
        if (binding.btnToggleLayout.isChecked) {
            binding.rvWayang.layoutManager = StaggeredGridLayoutManager(
                2,
                StaggeredGridLayoutManager.VERTICAL
            )
        } else {
            binding.rvWayang.layoutManager = LinearLayoutManager(this)
        }

        val adapterWayang = AdapterRecView(_arWayang)

        adapterWayang.setOnItemClickCallback(object : AdapterRecView.OnItemClickCallback {
            override fun onItemClicked(data: DcWayang) {
                val intentDetail = Intent(this@MainActivity, DetailActivity::class.java)

                intentDetail.putExtra("nama", data.nama)
                intentDetail.putExtra("karakter", data.karakter)
                intentDetail.putExtra("deskripsi", data.deskripsi)
                intentDetail.putExtra("foto", data.foto)

                startActivity(intentDetail)
            }

            override fun delData(position: Int) {
                val wayang = _arWayang[position]

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Konfirmasi hapus data")
                    .setMessage("Hapus ${wayang.nama} dari daftar?")
                    .setPositiveButton("Hapus") { _, _ ->
                        _arWayang.removeAt(position)
                        adapterWayang.notifyItemRemoved(position)

                        Toast.makeText(
                            this@MainActivity,
                            "${wayang.nama} berhasil dihapus",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        })

        binding.rvWayang.adapter = adapterWayang

        binding.rvWayang.itemAnimator = DefaultItemAnimator().apply {
            removeDuration = 500L
        }
    }
}