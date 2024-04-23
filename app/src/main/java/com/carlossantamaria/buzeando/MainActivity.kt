package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnCrearCuenta: Button
    private lateinit var btnMapa: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnCrearCuenta = findViewById(R.id.btnRegistrarCuenta)
        btnMapa = findViewById(R.id.btnMapa)

        btnCrearCuenta.setOnClickListener { abrirRegistro() }
        btnMapa.setOnClickListener { abrirMapa() }

    }

    private fun abrirRegistro() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun abrirMapa() {
        val intent = Intent(this, MapViewActivity::class.java)
        startActivity(intent)
    }
}