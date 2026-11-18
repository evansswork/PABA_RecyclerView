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
import android.widget.EditText
import android.widget.LinearLayout
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

        binding.btnTambahWayang.setOnClickListener {
            tampilkanFormTambahWayang(adapterWayang)
        }
    }

    private fun tampilkanFormTambahWayang(adapterWayang: AdapterRecView) {
        val form = LinearLayout(this)
        form.orientation = LinearLayout.VERTICAL

        val inputNama = EditText(this)
        inputNama.hint = "Nama wayang"
        form.addView(inputNama)

        val inputKarakter = EditText(this)
        inputKarakter.hint = "Karakter"
        form.addView(inputKarakter)

        val inputDeskripsi = EditText(this)
        inputDeskripsi.hint = "Deskripsi"
        form.addView(inputDeskripsi)

        val inputFoto = EditText(this)
        inputFoto.hint = "URL foto (opsional)"
        form.addView(inputFoto)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Tambah Wayang")
            .setView(form)
            .setNegativeButton("Batal", null)
            .setPositiveButton("Simpan", null)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val nama = inputNama.text.toString().trim()
            val karakter = inputKarakter.text.toString().trim()
            val deskripsi = inputDeskripsi.text.toString().trim()
            val foto = inputFoto.text.toString().trim()

            if (nama.isBlank() || karakter.isBlank() || deskripsi.isBlank()) {
                Toast.makeText(
                    this,
                    "Isi nama, karakter, dan deskripsi.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val wayangBaru = DcWayang(
                foto = foto,
                nama = nama,
                karakter = karakter,
                deskripsi = deskripsi
            )

            val posisiBaru = _arWayang.size
            _arWayang.add(wayangBaru)
            adapterWayang.notifyItemInserted(posisiBaru)
            binding.rvWayang.scrollToPosition(posisiBaru)

            dialog.dismiss()
        }
    }
}