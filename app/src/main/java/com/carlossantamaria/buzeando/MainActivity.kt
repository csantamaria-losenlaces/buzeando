package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnIniciarSesion: Button
    private lateinit var btnRegistro: Button
    private lateinit var btnMapa: Button
    private lateinit var btnNuevaOferta: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnIniciarSesion = findViewById(R.id.btnTengoCuenta)
        btnRegistro = findViewById(R.id.btnRegistrarCuenta)
        btnMapa = findViewById(R.id.btnMapa)
        btnNuevaOferta = findViewById(R.id.btnNuevaOferta)

        btnIniciarSesion.setOnClickListener { abrirIdentificar() }
        btnRegistro.setOnClickListener { abrirRegistro() }
        btnMapa.setOnClickListener { abrirMapa() }
        btnNuevaOferta.setOnClickListener { abrirNuevaOferta() }

    }

    private fun abrirIdentificar() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun abrirRegistro() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun abrirMapa() {
        val intent = Intent(this, MapViewActivity::class.java)
        startActivity(intent)
    }

    private fun abrirNuevaOferta() {
        val intent = Intent(this, AddOfferActivity::class.java)
        startActivity(intent)
    }
}