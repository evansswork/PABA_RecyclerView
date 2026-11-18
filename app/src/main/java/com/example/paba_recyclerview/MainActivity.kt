package com.example.paba_recyclerview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paba_recyclerview.databinding.ActivityMainBinding

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
        binding.rvWayang.layoutManager = LinearLayoutManager(this)
        binding.rvWayang.adapter = AdapterRecView(_arWayang)
    }
}