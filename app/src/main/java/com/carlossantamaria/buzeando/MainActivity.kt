package com.carlossantamaria.buzeando

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.carlossantamaria.buzeando.objects.User

class MainActivity : AppCompatActivity() {

    private lateinit var btnIniciarSesion: Button
    private lateinit var btnRegistro: Button
    private lateinit var tvCerrarSesion: TextView

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
        tvCerrarSesion = findViewById(R.id.tvCerrarSesion)

        btnIniciarSesion.setOnClickListener { abrirIdentificar() }
        btnRegistro.setOnClickListener { abrirRegistro() }
        tvCerrarSesion.setOnClickListener { cerrarSesion() }
    }

    override fun onResume() {
        super.onResume()
        if (User.id_usr.isNotBlank()) {
            btnIniciarSesion.isEnabled = false
            btnRegistro.isEnabled = false
            tvCerrarSesion.isVisible = true
            finish()
            abrirListaOfertas()
        }
    }

    private fun abrirIdentificar() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun abrirRegistro() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun cerrarSesion() {
        btnIniciarSesion.isEnabled = true
        btnRegistro.isEnabled = true
        tvCerrarSesion.isVisible = false
        User.cerrarSesion()
        Toast.makeText(this, "Has cerrado sesión", Toast.LENGTH_SHORT).show()
    }

    private fun abrirListaOfertas() {
        val intent = Intent(this, OfferListActivity::class.java)
        startActivity(intent)
    }

}